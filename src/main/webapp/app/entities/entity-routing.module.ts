import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cittadino',
        data: { pageTitle: 'benzappApp.cittadino.home.title' },
        loadChildren: () => import('./cittadino/cittadino.module').then(m => m.CittadinoModule),
      },
      {
        path: 'fascia',
        data: { pageTitle: 'benzappApp.fascia.home.title' },
        loadChildren: () => import('./fascia/fascia.module').then(m => m.FasciaModule),
      },
      {
        path: 'gestore',
        data: { pageTitle: 'benzappApp.gestore.home.title' },
        loadChildren: () => import('./gestore/gestore.module').then(m => m.GestoreModule),
      },
      {
        path: 'marchio',
        data: { pageTitle: 'benzappApp.marchio.home.title' },
        loadChildren: () => import('./marchio/marchio.module').then(m => m.MarchioModule),
      },
      {
        path: 'rifornimento',
        data: { pageTitle: 'benzappApp.rifornimento.home.title' },
        loadChildren: () => import('./rifornimento/rifornimento.module').then(m => m.RifornimentoModule),
      },
      {
        path: 'tessera',
        data: { pageTitle: 'benzappApp.tessera.home.title' },
        loadChildren: () => import('./tessera/tessera.module').then(m => m.TesseraModule),
      },
      {
        path: 'delega',
        data: { pageTitle: 'benzappApp.delega.home.title' },
        loadChildren: () => import('./delega/delega.module').then(m => m.DelegaModule),
      },
      {
        path: 'device',
        data: { pageTitle: 'benzappApp.device.home.title' },
        loadChildren: () => import('./device/device.module').then(m => m.DeviceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
