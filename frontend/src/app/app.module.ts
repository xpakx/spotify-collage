import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router'
import { HttpClientModule } from '@angular/common/http';
import { RedirectComponent } from './component/redirect/redirect.component';
import { FormsModule } from '@angular/forms';
import { TrackTableComponent } from './component/track-table/track-table.component';
import { PlaylistTableComponent } from './component/playlist-table/playlist-table.component';
import { PlaylistTracksTableComponent } from './component/playlist-tracks-table/playlist-tracks-table.component';

const routes: Routes = [
  { path: 'redirect', component: RedirectComponent },
  { path: 'playlists', component: PlaylistTableComponent },
  { path: 'playlists/:id', component: PlaylistTracksTableComponent }
]

@NgModule({
  declarations: [
    AppComponent,
    RedirectComponent,
    TrackTableComponent,
    PlaylistTableComponent,
    PlaylistTracksTableComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
