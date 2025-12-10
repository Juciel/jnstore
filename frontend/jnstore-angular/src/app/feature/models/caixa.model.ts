export interface CaixaInput {
  usuarioId?: number;
  valor?: number;
}

export interface CaixaRepresentation {
  id?: number;
  usuarioId?: number;
  dataAbertura?: string; // date-time
  valorInicial?: number;
  dataFechamento?: string; // date-time
  valorFinal?: number;
}
