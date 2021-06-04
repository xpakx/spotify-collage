import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StoreService } from '../../store-service.service';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private store: StoreService) { }

  ngOnInit(): void {
    let code = this.route.snapshot.queryParamMap.get("code");
    if(code) {
      this.store.code = code;
    }
    
    this.router.navigate(["/"]);
  }

}
