import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-variacao-tabela',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './variacao-tabela.component.html',
  styleUrls: ['./variacao-tabela.component.scss']
})
export class VariacaoTabelaComponent {
  @Input() variacoes: AbstractControl[] = [];
  @Input() podeEditar: boolean = false; // Recebe a permissão do componente pai
  @Input() parentForm: any; // Adicionado para receber o FormGroup do pai

  @Output() add = new EventEmitter<void>();
  @Output() edit = new EventEmitter<number>();
  @Output() remove = new EventEmitter<number>();

  editarVariacao(index: number) {
    this.edit.emit(index);
  }

  removerVariacao(index: number) {
    this.remove.emit(index);
  }
}
