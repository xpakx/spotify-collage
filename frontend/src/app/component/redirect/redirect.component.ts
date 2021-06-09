import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SpotifyService } from 'src/app/auth-service.service';
import { TokenResponse } from 'src/app/model/token-response';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router,
    private spotify: SpotifyService) { }

  ngOnInit(): void {
    let code = this.route.snapshot.queryParamMap.get("code");
    if(code) {
      this.spotify.getToken({code: code}).subscribe(
        (response: TokenResponse) => {
          localStorage.setItem("token", response.token);
          localStorage.setItem("username", response.username);
          
        },
        (error: HttpErrorResponse) => {
          //show error
        })
    }
    this.router.navigate(["/playlists"]);
  }

}
