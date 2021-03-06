import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { SpotifyService } from 'src/app/auth-service.service';
import { Page } from 'src/app/model/page';
import { Track } from 'src/app/model/track';

@Component({
  selector: 'app-track-table',
  templateUrl: './track-table.component.html',
  styleUrls: ['./track-table.component.css']
})
export class TrackTableComponent implements OnInit {
  tracks: Track[] = [];
  ready: boolean  = false;
  error: boolean  = false;
  imgSrc!: SafeUrl;
  collageLoad: boolean = false;

  constructor(private spotify: SpotifyService, private route: ActivatedRoute, 
    private sanitizer : DomSanitizer, private router: Router) { }

  ngOnInit(): void {
    let token = localStorage.getItem("token");
    if(token != null) {
      this.spotify.getBestTracks(token).subscribe(
        (response: Page<Track>) => {
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
      if(token != null) {
        this.collageLoad = true;
        this.spotify.getCollage(token).subscribe(
          (response: Blob) => { 
            this.getImage(response).subscribe(
              (result: string) => {
                this.imgSrc = this.sanitizer
                .bypassSecurityTrustUrl(result); 
                this.collageLoad = false;
              });
          },
          (error: HttpErrorResponse) => {
            this.error = true;  
            this.collageLoad = true;
          })
        }      
      }

  playlists() {
    this.router.navigate(["/playlists"]);
  }
}
