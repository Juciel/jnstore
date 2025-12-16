import { Component, Inject, PLATFORM_ID, ChangeDetectorRef, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormArray, Validators, FormControl } from '@angular/forms';
import { VendaService } from 'src/app/feature/services/venda.service';
import { ProdutoService } from 'src/app/feature/services/produto.service';
import { PerfilService } from 'src/app/feature/services/perfil.service';
import { PageProdutoRepresentation, ProdutoRepresetation, VariacaoProdutoRepresetation } from 'src/app/feature/models';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil, map } from 'rxjs/operators';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { forkJoin, of, Subject } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-venda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, CurrencyMaskDirective],
  templateUrl: './venda-form.component.html',
  styleUrls: ['./venda-form.component.scss'],
  providers: [CurrencyPipe]
})
export class VendaComponent implements OnInit, OnDestroy {
  form: any;
  saved: any = null;
  loading = false;
  submitError: string | null = null;

  // --- Totals ---
  totalItens: number = 0;
  private destroy$ = new Subject<void>();
  private isUpdatingTotals = false; // Flag para evitar loop infinito

  // --- Autocomplete Properties ---
  autocompleteResults: any[][] = [];
  parcelasCredito = [2, 3, 4, 5];

  podeEditarVenda = false;
  podeEditarValorVenda = false;
  podeVisualizarVenda = false;

  constructor(
    private fb: FormBuilder,
    private vendaService: VendaService,
    private produtoService: ProdutoService,
    private perfilService: PerfilService,
    private route: ActivatedRoute, private router: Router,
    @Inject(PLATFORM_ID) private platformId: any,
    private cdr: ChangeDetectorRef,
    private currencyPipe: CurrencyPipe
  ) {
    this.form = this.fb.group({
      itens: this.fb.array([]),
      pagamentos: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.permissoes();
    this.addItem();
    this.addPagamento();
    this.subscribeToChanges();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  permissoes(): void {
    this.loading = true;
    const requests = {
      podeEditarVenda: this.perfilService.podeEditarVenda().pipe(catchError(e => of(null))),
      podeVisualizarVenda: this.perfilService.podeVisualizarVenda().pipe(catchError(e => of(null))),
      podeEditarValorVenda: this.perfilService.podeEditarValorVenda().pipe(catchError(e => of(null))),
    };

    forkJoin(requests).subscribe({
      next: (data: any) => {
        this.podeEditarVenda = data.podeEditarVenda;
        this.podeVisualizarVenda = data.podeVisualizarVenda;
        this.podeEditarValorVenda = data.podeEditarValorVenda;

        // Itera sobre os controles de pagamento para aplicar a permissão
        for (let i = 0; i < this.pagamentos.length; i++) {
          const pagamentoControl = this.pagamentos.at(i);
          const valorPagoControl = pagamentoControl.get('valorPago');

          if (valorPagoControl) {
            if (!this.podeEditarValorVenda) {
              valorPagoControl.disable();
            } else {
              valorPagoControl.enable();
            }
          }
        }

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Erro ao carregar as pemissoes:', e);
        this.submitError = 'Erro ao carregar as pemissões.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private subscribeToChanges(): void {
    this.itens.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.updateTotals());
    this.pagamentos.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.updateTotals());
  }

  private updateTotals(): void {
    if (this.isUpdatingTotals) return;
    this.isUpdatingTotals = true;

    try {
      const items = this.itens.getRawValue();
      const formaPagamento = this.pagamentos.length > 0 ? this.pagamentos.at(0).get('forma')?.value : 'DINHEIRO';

      this.totalItens = items.reduce((total, item, index) => {
        if (!item.produto) return total;

        let precoUnitario = item.produto.valorVenda || 0;
        if (formaPagamento === 'CREDITO' && item.produto.valorVendaCredito) {
          precoUnitario = item.produto.valorVendaCredito;
        } else if (formaPagamento === 'PIX' && item.produto.valorVendaPix) {
          precoUnitario = item.produto.valorVendaPix;
        }

        const precoFormatado = this.currencyPipe.transform(precoUnitario, 'BRL', 'R$ ');
        const itemControl = this.itens.at(index);
        itemControl.get('precoUnitario')?.patchValue(precoFormatado, { emitEvent: false });

        const qtd = Number(item.quantidade) || 0;
        return total + (precoUnitario * qtd);
      }, 0);

      if (this.pagamentos.length > 0) {
        const valorPagoFormatado = this.currencyPipe.transform(this.totalItens, 'BRL', 'R$ ');
        this.pagamentos.at(0).get('valorPago')?.patchValue(valorPagoFormatado, { emitEvent: false });
      }
    } finally {
      this.isUpdatingTotals = false;
    }
    this.cdr.detectChanges();
  }

  get itens() { return this.form.get('itens') as FormArray; }
  get pagamentos() { return this.form.get('pagamentos') as FormArray; }

  private createItem() {
    const itemGroup = this.fb.group({
      search: new FormControl(''),
      produto: [null],
      varianteId: [null, Validators.required],
      precoUnitario: [{ value: 0, disabled: true }, [Validators.required, Validators.min(0.01)]],
      quantidade: [1, [Validators.required, Validators.min(1)]]
    });

    itemGroup.get('search')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        if (typeof value === 'string' && value.length > 1) {
          return this.produtoService.getAllPaginado(0, 5, [], value).pipe(map(res => res.content || []));
        }
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe(produtos => {
      const itemIndex = this.itens.controls.indexOf(itemGroup);
      if (itemIndex === -1) return;

      const results = produtos.flatMap(p =>
        p.variacoes ? p.variacoes.map(v => ({ produto: p, variacao: v })) : []
      );
      this.autocompleteResults[itemIndex] = results.slice(0, 10);
      this.cdr.detectChanges();
    });

    return itemGroup;
  }

  addItem() {
    this.itens.push(this.createItem());
    this.autocompleteResults.push([]);
  }

  removeItem(i: number) {
    this.itens.removeAt(i);
    this.autocompleteResults.splice(i, 1);
  }

  selectProduct(itemIndex: number, selected: { produto: ProdutoRepresetation, variacao: VariacaoProdutoRepresetation }) {
    const item = this.itens.at(itemIndex);
    const displayText = `(${selected.variacao.identificador}) ${selected.produto.nome} - ${selected.variacao.cor} - ${selected.variacao.tamanho}`;

    item.patchValue({
      produto: selected.produto,
      varianteId: selected.variacao.id,
      search: displayText
    }, { emitEvent: false });

    this.autocompleteResults[itemIndex] = [];
    this.updateTotals();
  }

  addPagamento() {
    const valorInicial = this.pagamentos.length === 0 ? this.totalItens : 0;
    this.pagamentos.push(this.fb.group({
      forma: ['DINHEIRO'],
      valorPago: [valorInicial, Validators.required],
      quantidadeParcelas: [2]
    }));
  }

  removePagamento(i: number) {
    this.pagamentos.removeAt(i);
    if (i === 0 && this.pagamentos.length > 0) {
      this.pagamentos.at(0).get('valorPago')?.setValue(this.totalItens);
    }
  }

  submit() {
    if (this.form.invalid) return;
    const raw = this.form.getRawValue();

    const cleanCurrency = (value: any): number | undefined => {
      if (value === null || value === undefined || value === '') return undefined;
      const num = Number(String(value).replace(/R\$\s?/g, '').replace(/\./g, '').replace(',', '.'));
      return isNaN(num) ? undefined : num;
    };

    const totalPago = (raw.pagamentos || []).reduce((acc: number, p: any) => acc + (cleanCurrency(p.valorPago) || 0), 0);
    const totalBruto = (raw.itens || []).reduce((acc: number, it: any) => acc + ((cleanCurrency(it.precoUnitario) || 0) * (it.quantidade || 0)), 0);

    const payload = {
      desconto: totalBruto > totalPago ? totalBruto - totalPago : 0,
      itens: (raw.itens || []).map((it: any) => ({
        varianteId: Number(it.varianteId),
        precoUnitario: cleanCurrency(it.precoUnitario),
        quantidade: Number(it.quantidade)
      })),
      totalBruto: totalBruto,
      pagamentos: (raw.pagamentos || []).map((p: any) => ({
        forma: p.forma,
        valorPago: cleanCurrency(p.valorPago),
        quantidadeParcelas: p.forma == 'CREDITO' ? Number(p.quantidadeParcelas) : 1
      })),
      totalLiquido: totalPago
    };

    this.loading = true;
    this.submitError = null;
    this.vendaService.registrarVenda(payload).subscribe({
      next: () => this.router.navigate(['/vendas'], { state: { successMessage: 'Venda registrada com sucesso!' } }),
      error: e => {
        this.submitError = e?.error?.message || e?.message || 'Erro ao registrar venda';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
