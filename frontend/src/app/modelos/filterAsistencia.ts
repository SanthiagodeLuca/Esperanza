export class filterAsistencia {

  private _startDate: string | undefined;
  private _endDate: string | undefined;

     constructor(startDate?: string, endDate?: string) {
    this._startDate = startDate;
    this._endDate = endDate;
  }
  
    // Getters
    get startDate(): String | undefined {
      return this._startDate;
    }
  
    get endDate(): String | undefined {
      return this._endDate;
    }
  
     // Setters
  set startDate(value: string | undefined) {
    this._startDate = value;
  }

  set endDate(value: string | undefined) {
    this._endDate = value;
  }
  }
  