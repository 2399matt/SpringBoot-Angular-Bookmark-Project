import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {TagResponse} from "./tag.service";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class BookMarkService {

  private apiUrl: string = environment.bookmarksApiUrl;

  constructor(private http: HttpClient) {

  }

  getBookMarks(searchTerm: string, activeTags?: number[], page?: number, pageSize?: number): Observable<PageBookMark> {
    let params = new HttpParams();
    if(searchTerm && searchTerm.trim().length > 0) {
      params = params.set('searchTerm', searchTerm);
    }
    if (activeTags && activeTags.length > 0) {
      params = params.set('tagIds', activeTags.join(','));
    }
    if (page) {
      params = params.set('page', page - 1);
    }
    if (pageSize) {
      params = params.set('size', pageSize);
    }
    return this.http.get<PageBookMark>(this.apiUrl, {params});
  }

  createBookMark(url: string, tagIds?: number[]): Observable<void> {
    let body;
    if(tagIds && tagIds.length > 0) {
       body = {url, tagIds};
    } else {
       body = {url};
    }
    return this.http.post<void>(this.apiUrl, body);
  }

  getBookMark(id: number): Observable<BookMark> {
    return this.http.get<BookMark>(`${this.apiUrl}/${id}`);
  }

  incrementViews(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/views/${id}`, null);
  }

  updateBookMark(id: number, title: string, description: string): Observable<BookMark> {
    const bookMarkRequest = {id, title, description};
    return this.http.put<BookMark>(`${this.apiUrl}`, bookMarkRequest);
  }

  deleteBookMark(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}


export interface BookMark {
  id: number,
  title: string,
  description: string,
  url: string,
  faviconUrl: string,
  createdAt: Date,
  views: number,
  tags: TagResponse[]
}

export interface PageBookMark {
  content: BookMark[],
  page: {
    size: number,
    number: number,
    totalElements: number,
    totalPages: number
  }
}

