import { Component, EventEmitter, Output, Inject, PLATFORM_ID, ChangeDetectorRef, AfterViewInit, ElementRef, ViewChild, OnDestroy } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ProdutoService } from '../../../services/produto.service';

// Declaração para o Bootstrap, para que o TypeScript não reclame.
declare var bootstrap: any;

@Component({
  selector: 'app-variacao-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './variacao-modal.component.html',
})
export class VariacaoModalComponent implements AfterViewInit, OnDestroy {
  @ViewChild('variacaoModal') modalElementRef!: ElementRef;
  @Output() save = new EventEmitter<{ index: number | null, data: any }>();

  form: FormGroup;
  modal: any;
  editIndex: number | null = null;
  private subscriptions = new Subscription();
  standardSizes = ['PP', 'P', 'M', 'G', 'GG', 'U'];
  standardColors = ['Amarelo', 'Azul', 'Bege', 'Branco', 'Cinza', 'Dourado', 'Laranja', 'Marrom', 'Ouro', 'Prata', 'Preto', 'Rosa', 'Roxo', 'Verde', 'Vermelho', 'Vinho'];


  constructor(
    private fb: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: any,
    private cdr: ChangeDetectorRef,
    private produtoService: ProdutoService
  ) {
    this.form = this.fb.group({
      id: [null],
      identificador: [{ value: '', disabled: true }],
      cor: ['', Validators.required],
      outraCor: [{ value: '', disabled: true }, Validators.required],
      tamanho: ['', Validators.required],
      outroTamanho: [{ value: '', disabled: true }, Validators.required],
      quantidadeEstoque: ['', [Validators.required, Validators.min(1)]]
    });

    this.subscribeToValueChanges();
  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.modal = new bootstrap.Modal(this.modalElementRef.nativeElement);
    }
  }

  private subscribeToValueChanges() {
    const corControl = this.form.get('cor');
    const outraCorControl = this.form.get('outraCor');
    if (corControl && outraCorControl) {
      const corSub = corControl.valueChanges.subscribe(value => {
        this.toggleCustomField(value, outraCorControl);
      });
      this.subscriptions.add(corSub);
    }

    const tamanhoControl = this.form.get('tamanho');
    const outroTamanhoControl = this.form.get('outroTamanho');
    if (tamanhoControl && outroTamanhoControl) {
      const tamanhoSub = tamanhoControl.valueChanges.subscribe(value => {
        this.toggleCustomField(value, outroTamanhoControl);
      });
      this.subscriptions.add(tamanhoSub);
    }
  }

  private toggleCustomField(value: string, control: AbstractControl | null) {
    if (!control) return;
    if (value === 'Outro') {
      control.enable();
    } else {
      control.disable();
      control.reset('');
    }
  }

  open(index: number | null = null, data?: AbstractControl | null) {
    this.editIndex = index;
    if (data) { // Editando
      this.form.patchValue(data.value);
      this.form.get('identificador')?.disable();

      const corValue = data.value.cor;
      if (!this.standardColors.includes(corValue) && corValue) {
        this.form.get('cor')?.setValue('Outro');
        this.form.get('outraCor')?.setValue(corValue);
      }

      const tamanhoValue = data.value.tamanho;
      if (!this.standardSizes.includes(tamanhoValue) && tamanhoValue) {
        this.form.get('tamanho')?.setValue('Outro');
        this.form.get('outroTamanho')?.setValue(tamanhoValue);
      }

    } else { // Adicionando
      this.form.reset({
        id: null,
        identificador: { value: 'Carregando...', disabled: true },
        cor: '',
        outraCor: { value: '', disabled: true },
        tamanho: '',
        outroTamanho: { value: '', disabled: true },
        quantidadeEstoque: ''
      });

      this.produtoService.getNewSku().subscribe({
        next: (sku) => this.form.get('identificador')?.setValue(sku),
        error: () => this.form.get('identificador')?.setValue('Erro ao gerar SKU')
      });
    }
    this.modal?.show();
    this.cdr.detectChanges();
  }

  close() {
    this.modal?.hide();
  }

  onSave() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formData = this.form.getRawValue();
    if (formData.cor === 'Outro') {
      formData.cor = formData.outraCor;
    }
    if (formData.tamanho === 'Outro') {
      formData.tamanho = formData.outroTamanho;
    }
    delete formData.outraCor;
    delete formData.outroTamanho;

    this.save.emit({
      index: this.editIndex,
      data: formData
    });
    this.close();
  }

  get f() {
    return this.form.controls;
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
