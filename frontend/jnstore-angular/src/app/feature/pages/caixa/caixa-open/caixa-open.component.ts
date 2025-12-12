import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router'; // Importar Router
import { CaixaService } from 'src/app/feature/services/caixa.service';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';

@Component({
  selector: 'app-caixa-open',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CurrencyMaskDirective],
  templateUrl: './caixa-open.component.html',
  styleUrls: ['./caixa-open.component.scss']
})
export class CaixaOpenComponent implements OnInit {
  valor?: number;
  opened: any = null;
  loading = false;
  openError: string | null = null;

  constructor(private caixaService: CaixaService, private cdr: ChangeDetectorRef, private router: Router) { } // Injetar Router

  ngOnInit(): void {
    this.verificarCaixaAberto();
  }

  verificarCaixaAberto() {
    this.loading = true;
    this.caixaService.consultaCaixaAbertoHoje().subscribe({
      next: (res: any) => {
        this.opened = res;
        if (this.opened && this.opened.dataFechamento) {
          // Se o caixa foi fechado, não o consideramos "aberto" nesta tela
          this.opened = null;
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e) => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrir() {
    if (this.opened) {
      this.openError = 'Um caixa já está aberto.';
      return;
    }
    if (!this.valor) {
      this.openError = 'O valor inicial é obrigatório.';
      return;
    }
    this.loading = true;
    this.openError = null;
    this.caixaService.abrirCaixa({ valor: this.valor }).subscribe({
      next: res => {
         this.router.navigate(['/caixa'], { state: { successMessage: 'Caixa aberto com sucesso!' } });
      },
      error: e => {
        this.openError = e?.error?.message || e?.message || 'Erro ao abrir caixa';
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (er) { /* ignore */ }
      }
    });
  }

  fechar() {
    if (!this.opened) {
      // Não há caixa aberto para fechar, pode-se adicionar uma mensagem de erro ou apenas retornar
      return;
    }
    // Navega para a rota de fechamento, passando o ID do caixa aberto
    this.router.navigate(['/caixa/fechar', this.opened.id]);
  }
}
