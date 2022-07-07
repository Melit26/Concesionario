import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'coche',
        data: { pageTitle: 'concesionarioApp.coche.home.title' },
        loadChildren: () => import('./coche/coche.module').then(m => m.CocheModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'concesionarioApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'detalle-venta',
        data: { pageTitle: 'concesionarioApp.detalleVenta.home.title' },
        loadChildren: () => import('./detalle-venta/detalle-venta.module').then(m => m.DetalleVentaModule),
      },
      {
        path: 'vendedor',
        data: { pageTitle: 'concesionarioApp.vendedor.home.title' },
        loadChildren: () => import('./vendedor/vendedor.module').then(m => m.VendedorModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'concesionarioApp.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
