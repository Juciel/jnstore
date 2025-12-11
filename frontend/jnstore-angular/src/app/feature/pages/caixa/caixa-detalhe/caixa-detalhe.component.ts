import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog, MatDialogModule } from '@angular/material/dialog'; // Importar MatDialog
import { CaixaService } from 'src/app/feature/services/caixa.service';
import { VendaService } from 'src/app/feature/services/venda.service';

import { catchError, of } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { VendaDetalheComponent } from 'src/app/feature/pages/venda/venda-detalhe/venda-detalhe.component'; // Importar VendaDetalheComponent
import { CaixaRepresentation, VendaRepresentation } from 'src/app/feature/models';

@Component({
  selector: 'app-caixa-detalhe',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  templateUrl: './caixa-detalhe.component.html',
  styleUrls: ['./caixa-detalhe.component.scss']
})
export class CaixaDetalheComponent implements OnInit {
  caixa: CaixaRepresentation | null = null;
  vendas: VendaRepresentation[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<CaixaDetalheComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { caixaId: number },
    private caixaService: CaixaService,
    private vendaService: VendaService,
    private dialog: MatDialog // Injetar MatDialog
  ) {}

  ngOnInit(): void {
    if (this.data.caixaId) {
      this.caixaService.buscarPorId(this.data.caixaId).pipe(
        catchError(err => {
          this.error = 'Não foi possível carregar os detalhes do caixa.';
          this.loading = false;
          return of(null);
        })
      ).subscribe(caixa => {
        if (caixa) {
          this.caixa = caixa;
          this.vendaService.getVendasByCaixaId(this.data.caixaId).pipe(
            catchError(err => {
              this.error = 'Não foi possível carregar as vendas do caixa.';
              return of([]);
            })
          ).subscribe(vendas => {
            this.vendas = vendas;
            this.loading = false;
          });
        } else {
          this.loading = false;
        }
      });
    }
  }

  abrirDetalheVenda(vendaId: number | undefined): void {
    if (!vendaId) return;

    this.dialog.open(VendaDetalheComponent, {
      data: { vendaId: vendaId },
      width: '600px'
    });
  }

  fechar(): void {
    this.dialogRef.close();
  }
}
