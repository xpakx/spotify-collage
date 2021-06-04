import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from './auth-service.service';
import { Collage } from './model/collage';
import { CollageRequest } from './model/collage-request';
import { SpotifyAddress } from './model/spotify-address';
import { StoreService } from './store-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  implements OnInit {
  title = 'spotify-collage';
  code: string | null = null;
  url!: string;
  terms: string[] = ['long_term', 'medium_term', 'short_term'];
  sizes: number[] = [3, 4, 5];

  collage!: Collage;
  

  constructor(private service: AuthServiceService, private route: ActivatedRoute, private router: Router, private store: StoreService) {
	  
	}
	  
  ngOnInit(): void {
     this.code = this.store.code;
    
    
    this.service.getAddress().subscribe(
      (response: SpotifyAddress) => {
        this.url = response.url;
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }

  getAlbums(result: {term: string, size: number}): void {
	  this.code = this.store.code;
    if(this.code===null) {return;}
    let request: CollageRequest = new CollageRequest(
      result.term ? result.term : 'long_term', 
      result.size ? result.size : 3, 
      this.code, 
      false
      );
    this.service.getCollage(request).subscribe(
      (response: Collage) => {
        this.collage = response;
        this.code = null;
      },
      (error: HttpErrorResponse) => {
        //show error
        this.code = null;
      }
    );
  }
}
