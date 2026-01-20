import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private baseUrl: string = environment.tagsApiUrl;

  constructor(private http: HttpClient) {

  }

  fetchTags(): Observable<TagResponse[]> {
    return this.http.get<TagResponse[]>(this.baseUrl);
  }
}

export interface TagResponse {
  id: number,
  name: string
}
