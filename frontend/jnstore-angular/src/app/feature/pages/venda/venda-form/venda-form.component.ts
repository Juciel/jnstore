import { Component, Inject, PLATFORM_ID, ChangeDetectorRef, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormArray, Validators, FormControl } from '@angular/forms';
import { VendaService } from 'src/app/feature/services/venda.service';
import { ProdutoService } from 'src/app/feature/services/produto.service';
import { PageProdutoRepresentation, ProdutoRepresetation, VariacaoProdutoRepresetation } from 'src/app/feature/models';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil, map } from 'rxjs/operators';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { of, Subject } from 'rxjs';

@Component({
  selector: 'app-venda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, CurrencyMaskDirective],
  templateUrl: './venda-form.component.html',
  styleUrls: ['./venda-form.component.scss']
})
export class VendaComponent implements OnInit, OnDestroy {
  form: any;
  saved: any = null;
  submitting = false;
  submitError: string | null = null;

  // --- Totals ---
  totalItens: number = 0;
  private destroy$ = new Subject<void>();

  // --- Autocomplete Properties ---
  autocompleteResults: any[][] = [];

  constructor(
    private fb: FormBuilder,
    private vendaService: VendaService,
    private produtoService: ProdutoService,
    private route: ActivatedRoute, private router: Router,
    @Inject(PLATFORM_ID) private platformId: any,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      itens: this.fb.array([]),
      pagamentos: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.addItem();
    this.addPagamento();
    this.subscribeToItemChanges();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private subscribeToItemChanges(): void {
    this.itens.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.updateTotals();
    });
  }

  private updateTotals(): void {
    const items = this.itens.getRawValue();
    this.totalItens = items.reduce((total, item) => {
      const preco = Number(item.precoUnitario) || 0;
      const qtd = Number(item.quantidade) || 0;
      return total + (preco * qtd);
    }, 0);

    if (this.pagamentos.length > 0) {
      this.pagamentos.at(0).get('valorPago')?.setValue(this.totalItens);
    }
    this.cdr.detectChanges();
  }

  get itens() { return this.form.get('itens') as FormArray; }
  get pagamentos() { return this.form.get('pagamentos') as FormArray; }

  private createItem() {
    const itemGroup = this.fb.group({
      search: new FormControl(''),
      varianteId: [null, Validators.required],
      precoUnitario: [{ value: 0, disabled: true }, [Validators.required, Validators.min(0.01)]],
      quantidade: [1, [Validators.required, Validators.min(1)]]
    });

    itemGroup.get('search')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        const itemIndex = this.itens.controls.indexOf(itemGroup);
        if (typeof value === 'string' && value.length > 1) {
          return this.produtoService.getAllPaginado(0, 5, [], value).pipe(
            map((response: PageProdutoRepresentation) => response.content || [])
          );
        }
        if (itemIndex !== -1) this.autocompleteResults[itemIndex] = [];
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe(produtos => {
      const itemIndex = this.itens.controls.indexOf(itemGroup);
      if (itemIndex === -1) return;

      const results: { produto: ProdutoRepresetation, variacao: VariacaoProdutoRepresetation }[] = [];
      for (const p of produtos) {
        if (p.variacoes) {
          for (const v of p.variacoes) {
            results.push({ produto: p, variacao: v });
          }
        }
      }
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
    item.patchValue({
      varianteId: selected.variacao.id,
      precoUnitario: selected.produto.valorVenda
    });

    const displayText = `(${selected.variacao.identificador})  ${selected.produto.nome} - ${selected.variacao.cor} - ${selected.variacao.tamanho} `;
    item.get('search')?.setValue(displayText, { emitEvent: false });
    this.autocompleteResults[itemIndex] = [];
  }

  addPagamento() {
    const valorInicial = this.pagamentos.length === 0 ? this.totalItens : 0;
    this.pagamentos.push(this.fb.group({ forma: ['DINHEIRO'], valorPago: [valorInicial, Validators.required] }));
  }

  removePagamento(i: number) {
    this.pagamentos.removeAt(i);
    // If we removed the first payment, the new first payment should get the total
    if (i === 0 && this.pagamentos.length > 0) {
      this.pagamentos.at(0).get('valorPago')?.setValue(this.totalItens);
    }
  }

  submit() {
    if (this.form.invalid) return;
    const raw: any = this.form.getRawValue();

    const totalPago = (raw.pagamentos || []).reduce((acc: number, p: any) => acc + Number(p.valorPago), 0);
    const desconto = this.totalItens > totalPago ? this.totalItens - totalPago : 0;

    const payload = {
      desconto: desconto,
      itens: (raw.itens || []).map((it: any) => ({
        varianteId: Number(it.varianteId),
        precoUnitario: Number(it.precoUnitario),
        quantidade: Number(it.quantidade)
      })),
      totalBruto : this.totalItens,
      pagamentos: (raw.pagamentos || []).map((p: any) => ({ forma: p.forma, valorPago: Number(p.valorPago) })),
      totalLiquido : totalPago
    };

    this.submitting = true;
    this.submitError = null;
    this.vendaService.registrarVenda(payload).subscribe({
      next: res => {
        this.router.navigate(['/vendas'], { state: { successMessage: 'Venda registrada com sucesso!' } });
      },
      error: e => {
        this.submitError = e?.error?.message || e?.message || 'Erro ao registrar venda';
        this.submitting = false;
        this.cdr.detectChanges();
      }
    });
  }
}
