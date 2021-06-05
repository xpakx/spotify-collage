import { Album } from "./album";

export interface Track {
    name: string;
    id: string;
    duration_ms: number;
    album: Album;
}