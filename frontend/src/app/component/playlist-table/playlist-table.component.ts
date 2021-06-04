import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/auth-service.service';
import { Page } from 'src/app/model/page';
import { Playlist } from 'src/app/model/playlist';

@Component({
  selector: 'app-playlist-table',
  templateUrl: './playlist-table.component.html',
  styleUrls: ['./playlist-table.component.css']
})
export class PlaylistTableComponent implements OnInit {
  playlists: Playlist[] = [];

  constructor(private spotify: AuthServiceService) { }

  ngOnInit(): void {
    let token = localStorage.getItem("token");
    if(token != null) {
      this.spotify.getPlaylists({token: token}).subscribe(
        (response: Page<Playlist>) => {
          this.playlists = response.items;          
        },
        (error: HttpErrorResponse) => {
          //show error
        })
      }
      
    }
  }
