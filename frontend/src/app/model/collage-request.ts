export class CollageRequest {
    
    constructor(public term: string, 
        public size: number, 
        public token: string, 
        public captions: boolean) {}
}