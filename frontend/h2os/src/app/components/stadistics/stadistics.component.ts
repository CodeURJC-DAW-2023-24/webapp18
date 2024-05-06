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
    data: (string | number) [][];
    columnNames: string[];
    public options: { title: string; colors: string[]; is3D: boolean; };
    width: number;
    height: number;
    ngOnInit(): void {
        this.title = 'Estadisticas de los socorristas';
        this.type = ChartType["PieChart"];
        this.width = 550;
        this.height = 400;
        this.data = [
            ['Confianza',     0],
            ['Esfuerzo',      0],
            ['Comunicación', 0],
            ['Actitud positiva', 0],
            ['Resolucion', 0],
            ['Liderazgo',   0]
          ];
        this.service.getPieChart().subscribe(
            response => {
                let aux1 = response as any
                this.pieChart = new Map<String, number>;
                for (const key in aux1) {
                    if (response.hasOwnProperty(key)) {
                        this.pieChart.set(key, aux1[key]);
                    }
                }
                
                console.log(this.pieChart)
                
                
                this.data = [
                    ['Confianza',     this.pieChart.get("Confianza")|| 0],
                    ['Esfuerzo',      this.pieChart.get("Esfuerzo")||0],
                    ['Comunicación',  this.pieChart.get("Comunicación")||0],
                    ['Actitud positiva', this.pieChart.get("Actitud positiva")||0],
                    ['Resolucion',    this.pieChart.get( "Resolución de problemas")||0],
                    ['Liderazgo',    this.pieChart.get("Liderazgo")||0]
                  ];


                
            },
            error => {
                console.error(error)
            }
        );
       
    }
}