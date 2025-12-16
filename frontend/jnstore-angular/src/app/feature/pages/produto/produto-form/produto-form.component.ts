import {
  Component, Inject, PLATFORM_ID, OnInit, ChangeDetectorRef, ViewChild
} from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProdutoService } from 'src/app/feature/services/produto.service';
import { CategoriaService } from 'src/app/feature/services/categoria.service';
import { TaxaService } from 'src/app/feature/services/taxa.service';
import { PerfilService } from 'src/app/feature/services/perfil.service';
import { CategoriaRepresetation, ProdutoRepresetation, TaxaRepresentation } from 'src/app/feature/models';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { forkJoin, finalize, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { VariacaoModalComponent } from 'src/app/feature/pages/produto/variacoes/variacao-modal.component';
import { VariacaoTabelaComponent } from 'src/app/feature/pages/produto/variacoes/variacao-tabela.component';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, CurrencyMaskDirective, VariacaoModalComponent, VariacaoTabelaComponent],
  templateUrl: './produto-form.component.html',
  styleUrls: ['./produto-form.component.scss']
})
export class ProdutoFormComponent implements OnInit {
  form: any;

  categorias: CategoriaRepresetation[] = [];
  taxa: TaxaRepresentation | null = null;
  loading = false;
  categoriasError: string | null = null;
  saveError: string | null = null;
  podeEditarProduto = false;
  podeVisualizarValorCompra = false;
  podeVisualizarProduto = false;

  isEditMode = false;
  produtoId: number | null = null;

  @ViewChild('variacaoModal') variacaoModal!: VariacaoModalComponent;

  constructor(private fb: FormBuilder,
              private produtoService: ProdutoService,
              private categoriaService: CategoriaService,
              private taxaService: TaxaService,
              private perfilService: PerfilService,
              private route: ActivatedRoute, private router: Router,
              @Inject(PLATFORM_ID) private platformId: any,
              private cdr: ChangeDetectorRef) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      descricao: [''],
      valorCompra: [''],
      valorVenda: ['', Validators.required],
      valorVendaPix: ['', Validators.required],
      valorVendaCredito: ['', Validators.required],
      categoriaId: ['', Validators.required],
      genero: ['', Validators.required],
      variacoes: this.fb.array([], Validators.minLength(1))
    });
  }

  ngOnInit(): void {
    this.permissoes();
    if (!isPlatformServer(this.platformId)) {
      this.loadCategorias();
      this.loadTaxas();
      this.route.params.subscribe(params => {
        if (params['id']) {
          this.produtoId = Number(params['id']);
          this.isEditMode = true;
          this.loadProduto();
        }
      });
    }
  }

  permissoes(): void{
    const requests = {
      podeEditarProduto: this.perfilService.podeEditarProduto().pipe(catchError(e => of(false))), // Retorna false em caso de erro
      podeVisualizarProduto: this.perfilService.podeVisualizarProduto().pipe(catchError(e => of(false))),
      podeVisualizarValorCompra: this.perfilService.podeVisualizarValorCompra().pipe(catchError(e => of(false))),
    }

    forkJoin(requests).subscribe({
      next: (data: any) => {
        this.podeEditarProduto = data.podeEditarProduto;
        this.podeVisualizarProduto = data.podeVisualizarProduto;
        this.podeVisualizarValorCompra = data.podeVisualizarValorCompra;

        if (!this.podeEditarProduto) {
          this.form.disable();
        } else {
          this.form.enable();
        }

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Erro ao carregar as pemissoes:', e);
        this.saveError = 'Erro ao carregar as pemissões.';
        this.form.disable(); // Desabilita o form em caso de erro
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onValorVendaBlur(): void {
    this.updateDependentValues();
  }

  private updateDependentValues() {
    const valorVendaControl = this.form.get('valorVenda');
    const valorNumerico = Number(valorVendaControl.value);

    if (isNaN(valorNumerico) || valorNumerico === 0) {
      this.form.patchValue({
        valorVendaPix: null,
        valorVendaCredito: null
      });
      return;
    }

    // 1. Atualiza o valor do PIX
    this.form.patchValue({ valorVendaPix: valorNumerico });

    // 2. Calcula e atualiza o valor do Crédito
    if (this.taxa && typeof this.taxa.valorTaxa === 'number') {
      const valorCredito = valorNumerico * (1 + this.taxa.valorTaxa / 100);
      this.form.patchValue({ valorVendaCredito: valorCredito });
    }
  }

  loadProduto(): void {
    if (!this.produtoId) return;
    this.loading = true;
    this.produtoService.getById(this.produtoId).subscribe({
      next: (p) => {
        this.form.patchValue({
          nome: p.nome,
          descricao: p.descricao,
          valorVenda: p.valorVenda,
          valorCompra: p.valorCompra,
          categoriaId: p.categoria?.id,
          genero: p.genero
        });

        // Após carregar o produto, chama o cálculo
        setTimeout(() => this.updateDependentValues(), 100);

        if (p.variacoes && p.variacoes.length) {
          while (this.variacoes.length) this.variacoes.removeAt(0);
          p.variacoes.forEach(v => {
            this.variacoes.push(this.fb.group({
              id: [v.id],
              identificador: [v.identificador],
              cor: [v.cor],
              tamanho: [v.tamanho],
              quantidadeEstoque: [v.quantidadeEstoque, Validators.required]
            }));
          });
        }
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.saveError = 'Erro ao carregar produto: ' + (e?.error?.message || e?.message || 'desconhecido');
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (er) { /* ignore */ }
      }
    });
  }

  get variacoes() {
    return this.form.get('variacoes') as FormArray;
  }

  openVariacaoModal(index?: number) {
    const variacao = index !== undefined ? this.variacoes.at(index) : undefined;
    this.variacaoModal.open(index, variacao);
  }

  onVariacaoSave(event: { index: number | null, data: any }) {
    if (event.index !== null && event.index >= 0) {
      this.variacoes.at(event.index).patchValue(event.data);
    } else {
      this.variacoes.push(this.fb.group({
        ...event.data,
        quantidadeEstoque: [event.data.quantidadeEstoque, [Validators.required, Validators.min(0)]]
      }));
    }
    this.cdr.detectChanges();
  }


  removeVariacao(i: number) {
    this.variacoes.removeAt(i);
  }

  submit() {
    const raw: any = this.form.getRawValue();

    const cleanCurrency = (value: any): number | undefined => {
      if (value === null || value === undefined || value === '') return undefined;
      if (typeof value === 'string') {
        const cleaned = value.replace(/R\$\s?/g, '').replace(/\./g, '').replace(',', '.');
        const num = Number(cleaned);
        return isNaN(num) ? undefined : num;
      }
      const num = Number(value);
      return isNaN(num) ? undefined : num;
    };

    const produto: ProdutoRepresetation = {
      nome: raw.nome ?? undefined,
      descricao: raw.descricao ?? undefined,
      valorCompra: cleanCurrency(raw.valorCompra),
      valorVenda: cleanCurrency(raw.valorVenda),
      valorVendaPix: cleanCurrency(raw.valorVendaPix),
      valorVendaCredito: cleanCurrency(raw.valorVendaCredito),
      genero: raw.genero ?? undefined,
      categoria: raw.categoriaId ? { id: Number(raw.categoriaId), descricao: '' } : undefined,
      variacoes: (raw.variacoes || []).map((v: any) => ({
        id: v.id,
        identificador: v.identificador ?? undefined,
        cor: v.cor ?? undefined,
        tamanho: v.tamanho ?? undefined,
        quantidadeEstoque: v.quantidadeEstoque ?? undefined
      }))
    };
    this.loading = true;
    this.saveError = null;
    try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }

    const saveObs = this.isEditMode && this.produtoId
      ? this.produtoService.update(this.produtoId, produto)
      : this.produtoService.create(produto);

    saveObs.subscribe({
      next: () => {
        this.router.navigate(['/produtos'], { state: { successMessage: 'Produto salvo com sucesso!' } });
      },
      error: (err) => {
        this.saveError = err?.error?.message || err?.message || 'Erro ao salvar produto';
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });
  }

  loadCategorias() {
    this.loading = true;
    this.categoriasError = null;
    this.categoriaService.listarTodas().pipe(
      finalize(() => {
        this.loading = false;
      })
    ).subscribe({ next: res => { this.categorias = res || []; try { this.cdr.detectChanges(); } catch (e) { /* ignore */ } }, error: e => { this.categoriasError = 'Erro ao carregar categoria: ' + (e?.error?.message || e?.message || 'desconhecido'); try { this.cdr.detectChanges(); } catch (er) { /* ignore */ } } });
  }

  loadTaxas() {
    this.loading = true;
    this.categoriasError = null;
    this.taxaService.getPorNome("CREDITO").pipe(
      finalize(() => {
        this.loading = false;
      })
    ).subscribe({
      next: res => {
        this.taxa = res;
        this.updateDependentValues();
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
        },
      error: e => {
        this.categoriasError = 'Erro ao carregar taxas: ' + (e?.error?.message || e?.message || 'desconhecido');
        try { this.cdr.detectChanges(); } catch (er) { /* ignore */ }
      }
    });
  }
}
