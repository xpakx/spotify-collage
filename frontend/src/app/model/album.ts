import { Artist } from "./artist";
import { Cover } from "./cover";

export interface Album {
    id: string;
    name: string;
    artists: Artist[];
    images: Cover[];
}