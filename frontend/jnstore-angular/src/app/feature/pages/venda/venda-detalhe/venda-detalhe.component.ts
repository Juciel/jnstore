import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatDialogModule,
  MAT_DIALOG_DATA,
  MatDialogRef
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { VendaService } from 'src/app/feature/services/venda.service';
import { VendaDetalheRepresentation } from 'src/app/feature/models';

@Component({
  selector: 'app-venda-detalhe',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  templateUrl: './venda-detalhe.component.html',
  styleUrls: ['./venda-detalhe.component.scss']
})
export class VendaDetalheComponent implements OnInit {
  venda: VendaDetalheRepresentation | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<VendaDetalheComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { vendaId: number },
    private vendaService: VendaService
  ) {}

  ngOnInit(): void {
    if (this.data.vendaId) {
      this.vendaService.detalharVenda(this.data.vendaId).subscribe({
        next: (venda) => {
          this.venda = venda;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Falha ao carregar detalhes da venda.';
          this.loading = false;
        }
      });
    }
  }

  close(): void {
    this.dialogRef.close();
  }
}
