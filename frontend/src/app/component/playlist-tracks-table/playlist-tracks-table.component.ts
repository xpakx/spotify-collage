import { HttpErrorResponse } from '@angular/common/http';
import { ReadVarExpr } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { observable, Observable, of, Subscriber } from 'rxjs';
import { SpotifyService } from 'src/app/auth-service.service';
import { Page } from 'src/app/model/page';
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
  imgSrc!: SafeUrl;

  constructor(private spotify: SpotifyService, private route: ActivatedRoute, private sanitizer : DomSanitizer) { }

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
  
  msToTime(s: number): string {
      var ms = s % 1000;
      s = (s - ms) / 1000;
      var secs = s % 60;
      s = (s - secs) / 60;
      var mins = s % 60;
      var hrs = (s - mins) / 60;
    
      return (hrs>0 ? hrs + ':' : '') + (mins>9 ? mins : '0' + mins) + ':' + (secs>9 ? secs : '0' + secs);
    }


    getImage(blob: Blob): Observable<string> {
      return Observable.create( (obs: any) => {
        const reader = new FileReader();
        reader.onerror = err => obs.error(err);
        reader.onabort = err => obs.error(err);
        reader.onload = () => obs.next(reader.result);
        reader.onloadend = () => obs.complete();
        return reader.readAsDataURL(blob);
      });
    }


    test(): void {
      let token = localStorage.getItem("token");
      let id = this.route.snapshot.paramMap.get("id");
      if(token != null && id != null) {
        this.spotify.getPlaylistCollage(token, id).subscribe(
          (response: Blob) => { 
            this.getImage(response).subscribe(
              (result: string) => {
                this.imgSrc = this.sanitizer
                .bypassSecurityTrustUrl(result); 
              });
          },
          (error: HttpErrorResponse) => {
            this.error = true;  
          })
        }      
      }
    

}
