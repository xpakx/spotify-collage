import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    let code = this.route.snapshot.queryParamMap.get("code");
    if(code) {
      localStorage.setItem("code", code);
    }
    this.router.navigate(["/"]);
  }

}
