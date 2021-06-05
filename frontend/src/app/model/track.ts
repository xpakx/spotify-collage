import { Album } from "./album";
import { Artist } from "./artist";

export interface Track {
    name: string;
    id: string;
    duration_ms: number;
    album: Album;
    artists: Artist[];
}