import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from 'src/app/auth-service.service';
import { TokenResponse } from 'src/app/model/token-response';
import { StoreService } from '../../store-service.service';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router,
    private spotify: AuthServiceService) { }

  ngOnInit(): void {
    let code = this.route.snapshot.queryParamMap.get("code");
    if(code) {
      alert(code);
      this.spotify.getToken({code: code}).subscribe(
        (response: TokenResponse) => {
          localStorage.setItem("token", response.token);
          localStorage.setItem("username", response.username);
        },
        (error: HttpErrorResponse) => {
          //show error
        })
    }
    
    this.router.navigate(["/"]);
  }

}
