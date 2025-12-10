export interface ErroDetalheRepresetation {
  campo?: string;
  mensagem?: string;
}

export interface ErroRepresetation {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  path?: string;
  detalhes?: ErroDetalheRepresetation[];
}
