import { Track } from "./track";

export interface TrackWrapper {
    track: Track;
    is_local: boolean;
    added_at: string;
}