import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CategoriaService } from 'src/app/feature/services/categoria.service';
import { PerfilService } from 'src/app/feature/services/perfil.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { CategoriaRepresetation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { FormsModule } from '@angular/forms'; // Importar FormsModule

@Component({
  selector: 'app-categoria-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule], // Adicionar FormsModule aqui
  templateUrl: './categoria-list.component.html',
  styleUrls: ['./categoria-list.component.scss']
})
export class CategoriaListComponent implements OnInit {
  categorias: CategoriaRepresetation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  // Propriedades de paginação e filtro
  currentPage: number = 0;
  pageSize: number = 5; // Você pode ajustar o tamanho da página
  totalPages: number = 0;
  totalElements: number = 0;
  descricaoFilter: string = '';

  // Propriedades de ordenação
  sortColumn: string = 'descricao';
  sortDirection: 'asc' | 'desc' = 'asc';
  sort: string[] = []; // Inicializado vazio, será preenchido em load()

  podeEditarCategoria = false;
  podeVisualizarCategoria = false;

  constructor(
    private categoriaService: CategoriaService,
    private confirmDialog: ConfirmDialogService,
    private perfilService: PerfilService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state?.['successMessage']) {
      this.successMessage = navigation.extras.state['successMessage'];
      setTimeout(() => this.successMessage = null, 5000); // Limpa a mensagem após 5 segundos

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
        podeEditarCategoria: this.perfilService.podeEditarCategoria().pipe(catchError(e => of(null))),
        podeVisualizarCategoria: this.perfilService.podeVisualizarCategoria().pipe(catchError(e => of(null))),
      }

      forkJoin(requests).subscribe({
        next: (data: any) => {
          this.podeEditarCategoria = data.podeEditarCategoria;
          this.podeVisualizarCategoria = data.podeVisualizarCategoria;
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
    this.sort = [`${this.sortColumn},${this.sortDirection}`]; // Atualiza o array sort
    this.categoriaService.getAllPaginado(this.currentPage, this.pageSize, this.sort, this.descricaoFilter).pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading categorias:', e);
        this.error = (e?.error?.message || e?.message || 'Erro ao carregar categorias');
        this.loading = false;
        this.cdr.detectChanges();
        return of({ content: [], totalPages: 0, totalElements: 0 }); // Retorna um objeto vazio para evitar erros
      })
    ).subscribe(res => {
      this.categorias = res.content || [];
      this.totalPages = res.totalPages || 0;
      this.totalElements = res.totalElements || 0;
      this.loading = false;
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
  }

  onFilterChange(): void {
    this.currentPage = 0; // Resetar para a primeira página ao aplicar o filtro
    this.load();
  }

  toggleSort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc'; // Padrão para asc ao mudar de coluna
    }
    this.currentPage = 0; // Resetar para a primeira página ao mudar a ordenação
    this.load();
  }

  onPageSizeChange(): void {
    this.currentPage = 0; // Resetar para a primeira página ao mudar o tamanho da página
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
    const maxPagesToShow = 5; // Número máximo de páginas para mostrar na paginação
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

  deleteCategoria(id: number | undefined, descricao: string | undefined): void {
    if (!id) return;
    const descricaoCategoria = descricao && descricao.trim() ? descricao : `ID ${id}`;
    this.confirmDialog.open({
      title: 'Deletar Categoria',
      message: `Tem certeza que deseja deletar "${descricaoCategoria}"? Esta ação não pode ser desfeita.`,
      okText: 'Deletar',
      cancelText: 'Cancelar',
      isDangerous: true
    }).subscribe(confirmed => {
      if (!confirmed) return;
      this.deleting = true;
      this.categoriaService.deletar(id).subscribe({
        next: () => {
          this.deleting = false;
          this.load(); // Recarregar a lista após a exclusão
          this.cdr.detectChanges();
        },
        error: (e) => {
          this.error = e?.error?.message || e?.message || 'Erro ao deletar categoria';
          this.deleting = false;
          this.cdr.detectChanges();
        }
      });
    });
  }
}
