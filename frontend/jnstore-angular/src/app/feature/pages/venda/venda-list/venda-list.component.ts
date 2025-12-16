import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { VendaService } from 'src/app/feature/services/venda.service';
import { PerfilService } from 'src/app/feature/services/perfil.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { VendaRepresentation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { VendaDetalheComponent } from 'src/app/feature/pages/venda/venda-detalhe/venda-detalhe.component';
import { FormsModule } from '@angular/forms'; // Importar FormsModule

@Component({
  selector: 'app-venda-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule], // Adicionar FormsModule aqui
  templateUrl: './venda-list.component.html',
  styleUrls: ['./venda-list.component.scss']
})
export class VendaListComponent implements OnInit {
  vendas: VendaRepresentation[] = [];
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
  sortColumn: string = 'id'; // Coluna padrão para ordenação
  sortDirection: 'desc' | 'asc' = 'desc'; // Padrão para vendas mais recentes
  sort: string[] = [];

  podeEditarVenda = false;
  podeEditarValorVenda = false;
  podeVisualizarVenda = false;

  constructor(
    private vendaService: VendaService,
    private confirmDialog: ConfirmDialogService,
    private perfilService: PerfilService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog,
    private router: Router
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
    this.permissoes();
    this.load();
  }

  permissoes(): void{
      this.loading = true;
      const requests = {
        podeEditarVenda: this.perfilService.podeEditarVenda().pipe(catchError(e => of(null))),
        podeVisualizarVenda: this.perfilService.podeVisualizarVenda().pipe(catchError(e => of(null))),
        podeEditarValorVenda: this.perfilService.podeEditarValorVenda().pipe(catchError(e => of(null))),
      }

      forkJoin(requests).subscribe({
        next: (data: any) => {
          this.podeEditarVenda = data.podeEditarVenda;
          this.podeVisualizarVenda = data.podeVisualizarVenda;
          this.podeEditarValorVenda = data.podeEditarValorVenda;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (e) => {
          console.error('Erro ao carregar as pemissoes:', e);
          this.error = 'Erro ao carregar as pemissões.';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.sort = [`${this.sortColumn},${this.sortDirection}`];

    // Passa as datas como string vazia ou undefined se não estiverem preenchidas
    const dataInicialParam = this.dataInicialFilter || undefined;
    const dataFinalParam = this.dataFinalFilter || undefined;

    this.vendaService.getAllPaginado(this.currentPage, this.pageSize, this.sort, dataInicialParam, dataFinalParam).pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading vendas:', e);
        this.error = (e?.error?.message || e?.message || 'Erro ao carregar vendas');
        this.loading = false;
        this.cdr.detectChanges();
        return of({ content: [], totalPages: 0, totalElements: 0 });
      })
    ).subscribe(res => {
      this.vendas = res.content || [];
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

  detalharVenda(id: number | undefined): void {
    if (!id) return;

    this.dialog.open(VendaDetalheComponent, {
      data: { vendaId: id },
      width: '600px'
    });
  }

  deleteVenda(id: number | undefined): void {
    if (!id) return;
    this.confirmDialog.open({
      title: 'Deletar Venda',
      message: `Tem certeza que deseja deletar a venda #${id}? Esta ação não pode ser desfeita.`,
      okText: 'Deletar',
      cancelText: 'Cancelar',
      isDangerous: true
    }).subscribe(confirmed => {
      if (!confirmed) return;
      this.deleting = true;
      this.vendaService.deletarVenda(id).subscribe({
        next: () => {
          this.load(); // Recarregar a lista após a exclusão
          this.deleting = false;
          this.cdr.detectChanges();
        },
        error: (e) => {
          this.error = (e?.error?.message || e?.message || 'Erro ao deletar venda');
          this.deleting = false;
          this.cdr.detectChanges();
        }
      });
    });
  }
}
