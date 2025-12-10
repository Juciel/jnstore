import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormArray, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-variacao-tabela',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './variacao-tabela.component.html',
  styleUrls: ['./variacao-tabela.component.scss']
})
export class VariacaoTabelaComponent {
  @Input() parentForm!: FormGroup;
  @Input() variacoes!: FormArray;
  @Output() add = new EventEmitter<void>();
  @Output() edit = new EventEmitter<number>();
  @Output() remove = new EventEmitter<number>();
}
