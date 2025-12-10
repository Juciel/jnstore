import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ProdutoService } from '../../services/produto.service';
import { ConfirmDialogService } from '../../components/confirm-dialog/confirm-dialog.service';
import { ProdutoRepresetation } from '../../models';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './produto-list.component.html',
  styleUrls: ['./produto-list.component.scss']
})
export class ProdutoListComponent implements OnInit {
  produtos: ProdutoRepresetation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  constructor(
    private produtoService: ProdutoService,
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
    console.log('Starting to load produtos...');
    this.produtoService.getAll().pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading produtos:', e);
        this.error = e?.error?.message || e?.message || 'Erro ao carregar produto';
        return of([]);
      })
    ).subscribe(res => {
      console.log('Response received:', res);
      this.produtos = Array.isArray(res) ? res : [];
      this.loading = false;
      console.log('Loading set to false');
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
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
          this.produtos = this.produtos.filter(p => p.id !== id);
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
