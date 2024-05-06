import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StadisticsService } from '../../services/stadistics.service';
import { ChartType } from 'angular-google-charts';



@Component({
    selector: "stadistics",
    templateUrl: './stadistics.component.html',
})
export class StadisticsComponent{

    constructor(private router:Router, private service:StadisticsService){}

    pieChart: Map<String, number>;
    trust: number;

    // Atributes
    reliability: boolean;
    effort: boolean;
    communication: boolean;
    attitude: boolean;
    problemsResolution: boolean;
    leadership: boolean;

    // PieChart parameters
    title: string;
    type: ChartType;
    data: (string | number)[][];
    columnNames: string[];
    public options: { title: string; colors: string[]; is3D: boolean; };
    width: number;
    height: number;

    ngOnInit(): void {
        this.service.getPieChart().subscribe(
            pieChart => {
                this.pieChart = pieChart as Map<String, number>;
            },
            error => {
                console.error(error)
            }
        );
        this.title = 'Lifeguard stadistics';
        this.type = ChartType["PieChart"];
        this.data = [
          ['Confianza',     this.pieChart.get("trust")],
          ['Esfuerzo',      this.pieChart.get("effort")],
          ['Comunicación',  this.pieChart.get("comunication")],
          ['Actitud positiva', this.pieChart.get("attitude")],
          ['Resolucion',    this.pieChart.get("resolution")],
          ['Liderazgo',    this.pieChart.get("leadership")]

        ];
        this.width = 550;
        this.height = 400;
    }
}