import { Component, OnInit } from '@angular/core';
import {Leader} from "../game/leader";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.scss']
})
export class LeaderboardComponent implements OnInit {

  leaders!: Leader[];

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.leaders = this.route.snapshot.data['leaderboard'];
  }
}
