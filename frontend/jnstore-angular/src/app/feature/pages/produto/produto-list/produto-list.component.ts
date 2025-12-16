import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ProdutoService } from 'src/app/feature/services/produto.service';
import { PerfilService } from 'src/app/feature/services/perfil.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { ProdutoRepresetation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { FormsModule } from '@angular/forms'; // Importar FormsModule

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule], // Adicionar FormsModule aqui
  templateUrl: './produto-list.component.html',
  styleUrls: ['./produto-list.component.scss']
})
export class ProdutoListComponent implements OnInit {
  produtos: ProdutoRepresetation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  // Propriedades de paginação e filtro
  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  totalElements: number = 0;
  nomeFilter: string = '';

  // Propriedades de ordenação
  sortColumn: string = 'nome';
  sortDirection: 'asc' | 'desc' = 'asc';
  sort: string[] = [];

  podeEditarProduto = false;
  podeVisualizarValorCompra = false;
  podeVisualizarProduto = false;

  constructor(
    private produtoService: ProdutoService,
    private confirmDialog: ConfirmDialogService,
    private perfilService: PerfilService,
    private cdr: ChangeDetectorRef,
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
        podeEditarProduto: this.perfilService.podeEditarProduto().pipe(catchError(e => of(null))),
        podeVisualizarProduto: this.perfilService.podeVisualizarProduto().pipe(catchError(e => of(null))),
        podeVisualizarValorCompra: this.perfilService.podeVisualizarValorCompra().pipe(catchError(e => of(null))),
      }

      forkJoin(requests).subscribe({
        next: (data: any) => {
          this.podeEditarProduto = data.podeEditarProduto;
          this.podeVisualizarProduto = data.podeVisualizarProduto;
          this.podeVisualizarValorCompra = data.podeVisualizarValorCompra;
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
    this.produtoService.getAllPaginado(this.currentPage, this.pageSize, this.sort, this.nomeFilter).pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading produtos:', e);
        this.error = (e?.error?.message || e?.message || 'Erro ao carregar produtos');
        this.loading = false;
        this.cdr.detectChanges();
        return of({ content: [], totalPages: 0, totalElements: 0 });
      })
    ).subscribe(res => {
      this.produtos = res.content || [];
      this.totalPages = res.totalPages || 0;
      this.totalElements = res.totalElements || 0;
      this.loading = false;
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.load();
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

  deleteProduto(id: number | undefined, nome: string | undefined): void {
    if (!id) return;
    const nomeProduto = nome && nome.trim() ? nome : `ID ${id}`;
    this.confirmDialog.open({
      title: 'Deletar Produto',
      message: `Tem certeza que deseja deletar "${nomeProduto}"? Esta ação não pode ser desfeita.`,
      okText: 'Deletar',
      cancelText: 'Cancelar',
      isDangerous: true
    }).subscribe(confirmed => {
      if (!confirmed) return;
      this.deleting = true;
      this.produtoService.delete(id).subscribe({
        next: () => {
          this.load(); // Recarregar a lista após a exclusão
          this.deleting = false;
          this.cdr.detectChanges();
        },
        error: (e) => {
          this.error = e?.error?.message || e?.message || 'Erro ao deletar produto';
          this.deleting = false;
          this.cdr.detectChanges();
        }
      });
    });
  }
}
