import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CaixaService } from 'src/app/feature/services/caixa.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { CaixaRepresentation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CaixaDetalheComponent } from 'src/app/feature/pages/caixa/caixa-detalhe/caixa-detalhe.component';
import { FormsModule } from '@angular/forms'; // Importar FormsModule

@Component({
  selector: 'app-caixa-list',
  standalone: true,
  imports: [CommonModule, RouterLink, MatDialogModule, FormsModule], // Adicionar FormsModule
  templateUrl: './caixa-list.component.html',
  styleUrls: ['./caixa-list.component.scss']
})
export class CaixaListComponent implements OnInit {
  caixas: CaixaRepresentation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  // Propriedades de paginação e filtro
  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  totalElements: number = 0;
  dataInicialFilter: string = ''; // Filtro por data inicial (YYYY-MM-DD do input)
  dataFinalFilter: string = '';   // Filtro por data final (YYYY-MM-DD do input)

  // Propriedades de ordenação
  sortColumn: string = 'dataAbertura'; // Coluna padrão para ordenação
  sortDirection: 'desc' | 'asc' = 'desc'; // Padrão para caixas mais recentes

  sort: string[] = [];

  constructor(
    private caixaService: CaixaService,
    private confirmDialog: ConfirmDialogService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private dialog: MatDialog
  ) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state?.['successMessage']) {
      this.successMessage = navigation.extras.state['successMessage'];
      setTimeout(() => this.successMessage = null, 5000);

      // Limpar o estado de navegação para que a mensagem não persista em recarregamentos ou navegações futuras
      const state = { ...history.state };
      delete state.successMessage;
      history.replaceState(state, '', this.router.url);
    }
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.sort = [`${this.sortColumn},${this.sortDirection}`];

    // Passa as datas como string vazia ou undefined se não estiverem preenchidas
    const dataInicialParam = this.dataInicialFilter || undefined;
    const dataFinalParam = this.dataFinalFilter || undefined;

    this.caixaService.getAllPaginado(this.currentPage, this.pageSize, this.sort, dataInicialParam, dataFinalParam).pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading caixas:', e);
        this.error = (e?.error?.message || e?.message || 'Erro ao carregar caixas');
        this.loading = false;
        this.cdr.detectChanges();
        return of({ content: [], totalPages: 0, totalElements: 0 });
      })
    ).subscribe(res => {
      this.caixas = res.content || [];
      this.totalPages = res.totalPages || 0;
      this.totalElements = res.totalElements || 0;
      this.loading = false;
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
  }

  onFilterChange(): void {
    this.currentPage = 0; // Sempre resetar a página na mudança de filtro
    const hasDataInicial = !!this.dataInicialFilter;
    const hasDataFinal = !!this.dataFinalFilter;

    if ((hasDataInicial && hasDataFinal) || (!hasDataInicial && !hasDataFinal)) {
      // Chama load() se ambas as datas estão preenchidas ou se nenhuma está preenchida
      this.load();
    }
  }

  toggleSort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.currentPage = 0;
    this.load();
  }

  onPageSizeChange(): void {
    this.currentPage = 0;
    this.load();
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages && page !== this.currentPage) {
      this.currentPage = page;
      this.load();
    }
  }

  getPagesArray(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    let startPage = Math.max(0, this.currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(this.totalPages - 1, startPage + maxPagesToShow - 1);

    if (endPage - startPage + 1 < maxPagesToShow) {
      startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  fecharCaixa(id: number | undefined): void {
    if (!id) return;
    this.confirmDialog.open({
      title: 'Fechar Caixa',
      message: `Tem certeza que deseja fechar o caixa ${id}?`,
      okText: 'Fechar',
      cancelText: 'Cancelar',
      isDangerous: true
    }).subscribe(confirmed => {
      if (!confirmed) return;
      this.deleting = true;
      this.caixaService.fecharCaixa(id, {}).subscribe({
        next: (updatedCaixa) => {
          this.load(); // Recarregar a lista após a exclusão
          this.deleting = false;
          this.successMessage = `Caixa ${id} fechado com sucesso!`;
          setTimeout(() => this.successMessage = null, 5000);
          this.cdr.detectChanges();
        },
        error: (e) => {
          this.error = e?.error?.message || e?.message || 'Erro ao fechar caixa';
          this.deleting = false;
          this.cdr.detectChanges();
        }
      });
    });
  }

  detalharCaixa(id: number | undefined): void {
    if (!id) return;

    this.dialog.open(CaixaDetalheComponent, {
      data: { caixaId: id },
      width: '800px'
    });
  }

  deleteCaixa(id: number | undefined): void {
    if (!id) return;
    this.confirmDialog.open({
      title: 'Deletar Caixa',
      message: `Tem certeza que deseja deletar o caixa ${id}? Esta ação não pode ser desfeita.`,
      okText: 'Deletar',
      cancelText: 'Cancelar',
      isDangerous: true
    }).subscribe(confirmed => {
      if (!confirmed) return;
      this.deleting = true;
      this.caixaService.delete(id).subscribe({
        next: () => {
          this.load();
          this.deleting = false;
          this.successMessage = `Caixa ${id} deletado com sucesso!`;
          setTimeout(() => this.successMessage = null, 5000);
          this.cdr.detectChanges();
        },
        error: (e) => {
          this.error = e?.error?.message || e?.message || 'Erro ao deletar caixa';
          this.deleting = false;
          this.cdr.detectChanges();
        }
      });
    });
  }
}
