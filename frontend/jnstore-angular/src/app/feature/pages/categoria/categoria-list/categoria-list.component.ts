import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CategoriaService } from 'src/app/feature/services/categoria.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { CategoriaRepresetation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-categoria-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './categoria-list.component.html',
  styleUrls: ['./categoria-list.component.scss']
})
export class CategoriaListComponent implements OnInit {
  categorias: CategoriaRepresetation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  constructor(
    private categoriaService: CategoriaService,
    private confirmDialog: ConfirmDialogService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state?.['successMessage']) {
      this.successMessage = navigation.extras.state['successMessage'];
      setTimeout(() => this.successMessage = null, 5000); // Limpa a mensagem após 5 segundos
    }
  }


  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    console.log('Starting to load categorias...');
    this.categoriaService.listarTodas().pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading categorias:', e);
        this.error = (e?.error?.message || e?.message || 'Erro ao carregar categorias');
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(res => {
      console.log('Response received:', res);
      this.categorias = Array.isArray(res) ? res : [];
      this.loading = false;
      console.log('Loading set to false');
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
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
          this.categorias = this.categorias.filter(c => c.id !== id);
          this.deleting = false;
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
