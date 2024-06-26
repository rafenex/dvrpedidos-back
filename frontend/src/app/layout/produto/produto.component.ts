import { Component } from "@angular/core";
import { Produto, ProdutoForm } from "../../models/produto/produto";

import { CategoriaResponse } from "../../models/categoria/categoria";
import { ConfirmationService, MessageService } from "primeng/api";
import { ApiService } from "../../services/api.service";

@Component({
  selector: "app-produto",
  templateUrl: "./produto.component.html",
  styleUrl: "./produto.component.scss",
})
export class ProdutoComponent {
  constructor(
    private apiService: ApiService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}
  categorias = [] as CategoriaResponse[];
  produtos: Produto[] = [];
  produtoEdit = {} as ProdutoForm;
  displayedColumns = [
    "referencia",
    "nome",
    "valorPadrao",
    "categoria",
    "acoes",
  ];
  openDialog = false;
  totalElements: number = 0;
  params: any = {
    referencia: null,
    nome: null,
    page: 0,
    size: 5,
  };

  ngOnInit(): void {
    this.getProdutos();
    this.getCategorias();
  }

  pageChange(event: any) {
    this.params.page = event.first / event.rows;
    this.params.size = event.rows;
    this.getProdutos();
  }

  handleFilter() {
    this.params.page = 0;
    this.params.size = 5;
    this.getProdutos();
  }

  getProdutos() {
    this.apiService
      .get<Produto[]>(`/produtos?`, this.params)
      .subscribe((res: any) => {
        this.produtos = res.content;
        this.totalElements = res.totalElements;
      });
  }

  addProduto(produto: Produto) {
    this.apiService.post(`/produtos`, produto).subscribe({
      next: () => {
        this.messageService.add({
          severity: "info",
          summary: "Confirmado",
          detail: "Produto adicionado",
        });
        this.getProdutos();
      },
      error: (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Erro: " + error.status,
          detail: "Ocorreu um erro ao adicionar produto",
        });
      },
    });
  }

  updateProduto(produto: Produto) {
    this.apiService.put(`/produtos`, produto.id, produto).subscribe({
      next: () => {
        this.messageService.add({
          severity: "info",
          summary: "Confirmado",
          detail: "Produto editado",
        });
        this.getProdutos();
      },
      error: (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Erro: " + error.status,
          detail: "Ocorreu um erro ao editar produto",
        });
      },
    });
  }
  getCategorias() {
    this.apiService
      .get<CategoriaResponse[]>("/categorias?", { page: 0, size: 1000 })
      .subscribe((res: any) => (this.categorias = res.content));
  }

  deleteProduto(produto: Produto) {
    this.apiService.delete(`/produtos`, produto.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: "info",
          summary: "Confirmado",
          detail: "Produto removido",
        });
        this.getProdutos();
      },
      error: (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Erro: " + error.status,
          detail: "Ocorreu um erro ao deletar produto",
        });
      },
    });
  }

  deleteDialog(event: Event, produto: Produto) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: `Deseja remover o produto ${produto.nome}?`,
      header: "Confirmação de remoção",
      icon: "pi pi-info-circle",
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.deleteProduto(produto);
      },
      reject: () => {
        this.messageService.add({
          severity: "error",
          summary: "Rejeitado",
          detail: "Você rejeitou a ação",
        });
      },
    });
  }

  showFormDialog(show: boolean, produto?: ProdutoForm) {
    this.produtoEdit = {} as ProdutoForm;
    if (produto) {
      this.produtoEdit = produto;
    }
    this.openDialog = show;
  }
}
