import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthServiceService } from 'src/app/auth-service.service';
import { Page } from 'src/app/model/page';
import { Track } from 'src/app/model/track';
import { TrackWrapper } from 'src/app/model/track-wrapper';

@Component({
  selector: 'app-playlist-tracks-table',
  templateUrl: './playlist-tracks-table.component.html',
  styleUrls: ['./playlist-tracks-table.component.css']
})
export class PlaylistTracksTableComponent implements OnInit {
  tracks: TrackWrapper[] = [];
  ready: boolean  = false;
  error: boolean  = false;

  constructor(private spotify: AuthServiceService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    let token = localStorage.getItem("token");
    let id = this.route.snapshot.paramMap.get("id");
    if(token != null && id != null) {
      this.spotify.getPlaylistTracks({token: token}, id).subscribe(
        (response: Page<TrackWrapper>) => {
          this.tracks = response.items;
          this.ready = true;       
        },
        (error: HttpErrorResponse) => {
          this.ready = true;
          this.error = true;  
        })
      }      
    }
}
