import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CaixaService } from '../../services/caixa.service';
import { ConfirmDialogService } from '../../components/confirm-dialog/confirm-dialog.service';
import { CaixaRepresentation } from '../../models';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-caixa-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './caixa-list.component.html',
  styleUrls: ['./caixa-list.component.scss']
})
export class CaixaListComponent implements OnInit {
  caixas: CaixaRepresentation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;
  successMessage: string | null = null;

  constructor(
    private caixaService: CaixaService,
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
    this.caixaService.listarCaixas().pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading caixas:', e);
        this.error = e?.error?.message || e?.message || 'Erro ao carregar caixas';
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(res => {
      this.caixas = Array.isArray(res) ? res : [];
      this.loading = false;
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
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
      this.deleting = true; // Usando 'deleting' para indicar operação em andamento
      this.caixaService.fecharCaixa(id, {}).subscribe({ // Assumindo que fecharCaixa pode receber um objeto vazio ou um CaixaInput com dados relevantes
        next: (updatedCaixa) => {
          this.caixas = this.caixas.map(c => c.id === id ? updatedCaixa : c);
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
          this.caixas = this.caixas.filter(c => c.id !== id);
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
