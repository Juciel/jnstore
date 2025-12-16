import { Directive, ElementRef, HostListener, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { NgControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil, distinctUntilChanged } from 'rxjs/operators';

@Directive({
  selector: '[currencyMask]',
  standalone: true
})
export class CurrencyMaskDirective implements OnInit, AfterViewInit, OnDestroy {
  private formatter = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
  private destroy$ = new Subject<void>();
  private isFocused = false;

  constructor(private el: ElementRef<HTMLInputElement>, private control: NgControl) {}

  ngOnInit(): void {
    // Observa mudanças no valor do FormControl
    this.control.control?.valueChanges.pipe(
      takeUntil(this.destroy$),
      distinctUntilChanged()
    ).subscribe(value => {
      // Formata o valor se o campo não estiver em foco e se o valor for numérico.
      // Isso lida com atualizações programáticas, como o patchValue.
      if (!this.isFocused && this.isNumeric(value)) {
        this.writeFormatted(Number(value));
      }
    });
  }

  ngAfterViewInit(): void {
    // Garante que o valor inicial seja formatado, especialmente em cenários de edição.
    // O setTimeout ajuda a garantir que o valor do formulário já tenha sido definido.
    setTimeout(() => {
      const initialValue = this.getModelValue();
      if (initialValue !== null && !this.isFocused) {
        this.writeFormatted(initialValue);
      }
    }, 100); // Um pequeno delay para aguardar o ciclo de vida do Angular
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private getModelValue(): number | null {
    const value = this.control?.control?.value;
    if (value === null || value === '' || !this.isNumeric(value)) {
      return null;
    }
    return Number(value);
  }

  private isNumeric(value: any): boolean {
    return value !== null && value !== '' && !isNaN(Number(value));
  }

  private writeFormatted(n: number) {
    try {
      this.el.nativeElement.value = this.formatter.format(n);
    } catch {
      this.el.nativeElement.value = String(n);
    }
  }

  @HostListener('focus')
  onFocus() {
    this.isFocused = true;
    const value = this.getModelValue();
    // Ao focar, exibe o número puro com vírgula para facilitar a edição
    this.el.nativeElement.value = value !== null ? String(value).replace('.', ',') : '';
    this.el.nativeElement.select();
  }

  @HostListener('input', ['$event'])
  onInput(event: Event) {
    const input = this.el.nativeElement as HTMLInputElement;
    const raw = input.value || '';

    // 1. Limpa o valor, mantendo apenas os dígitos
    let digits = raw.replace(/\D/g, '');

    // Evita que o valor seja "0" se o campo estiver vazio
    if (digits === '') {
      this.control.control?.setValue(null, { emitEvent: false });
      input.value = '';
      return;
    }

    // 2. Converte para número, dividindo por 100 para ter os centavos
    const num = Number(digits) / 100;

    // 3. Formata o número para o padrão BRL
    const formatted = this.formatter.format(num);

    // Atualiza o modelo com o valor numérico sem emitir evento para evitar loops
    try { this.control.control?.setValue(num, { emitEvent: false }); } catch {}

    // Escreve o valor formatado e tenta restaurar a posição do cursor
    try {
      input.value = formatted;
      // Mantém o cursor no final, que é o comportamento esperado para digitação de moeda
      const newCursorPosition = formatted.length;
      input.setSelectionRange(newCursorPosition, newCursorPosition);
    } catch {
      // Fallback: coloca o cursor no final
      try { input.setSelectionRange(input.value.length, input.value.length); } catch {}
    }
  }

  @HostListener('blur')
  onBlur() {
    this.isFocused = false;
    const value = this.getModelValue();
    if (value !== null) {
      this.writeFormatted(value);
    } else {
      this.el.nativeElement.value = '';
    }
  }
}
