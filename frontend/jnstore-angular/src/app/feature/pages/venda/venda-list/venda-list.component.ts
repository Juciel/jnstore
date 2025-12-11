import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { VendaService } from 'src/app/feature/services/venda.service';
import { ConfirmDialogService } from 'src/app/feature/components/confirm-dialog/confirm-dialog.service';
import { VendaRepresentation } from 'src/app/feature/models';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { VendaDetalheComponent } from 'src/app/feature/pages/venda/venda-detalhe/venda-detalhe.component';

@Component({
  selector: 'app-venda-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './venda-list.component.html',
  styleUrls: ['./venda-list.component.scss']
})
export class VendaListComponent implements OnInit {
  vendas: VendaRepresentation[] = [];
  loading = false;
  error: string | null = null;
  deleting = false;

  constructor(
    private vendaService: VendaService,
    private confirmDialog: ConfirmDialogService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    console.log('Starting to load vendas...');
    this.vendaService.listarVendas().pipe(
      timeout(10000),
      catchError(e => {
        console.error('Error loading vendas:', e);
        this.error = e?.message || 'Erro ao carregar vendas';
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(res => {
      console.log('Response received:', res);
      this.vendas = Array.isArray(res) ? res : [];
      this.loading = false;
      console.log('Loading set to false');
      try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
    });
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
        next: () => { this.vendas = this.vendas.filter(v => v.id !== id); this.deleting = false; this.cdr.detectChanges();},
        error: (e) => { this.error = (e?.error?.message || e?.message || 'Erro ao deletar venda'); this.deleting = false; this.cdr.detectChanges();}
      });
    });
  }
}
