export class filterAsistencia {
    private _startDate?: Date;
    private _endDate?: Date;
  
    constructor(startDate?: Date, endDate?: Date) {
      this._startDate = startDate;
      this._endDate = endDate;
    }
  
    // Getters
    get startDate(): Date | undefined {
      return this._startDate;
    }
  
    get endDate(): Date | undefined {
      return this._endDate;
    }
  
    // Setters
    set startDate(value: Date | undefined) {
      this._startDate = value;
    }
  
    set endDate(value: Date | undefined) {
      this._endDate = value;
    }
  }
  