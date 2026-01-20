import {Component} from '@angular/core';
import {TagResponse, TagService} from "../../services/tag.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.css'
})
export class SideBarComponent {

  activeTags: number[] = [];
  tags: TagResponse[] = [];

  constructor(private tagService: TagService, private router: Router) {
  }

  ngOnInit(): void {
    this.getTags();
  }

  getTags(): void {
    this.tagService.fetchTags().subscribe({
      next: data => {
        this.tags = data;
      }
    });
  }

  onTagSelected(id: number): void {
    if (!this.activeTags.includes(id)) {
      this.activeTags.push(id);
      console.log('TAGS: ' + this.activeTags);
    } else {
      this.activeTags.splice(this.activeTags.indexOf(id), 1);
    }
    this.router.navigate(['/bookmarks'],
      {
        queryParams:
          {
            tags: this.activeTags.join(',')
          }, queryParamsHandling: 'merge'
      }
    );
  }
}
