import { Component, AfterViewInit } from '@angular/core';
import { StadisticsService } from '../../services/stadistics.service';
import { ChartType } from 'angular-google-charts';
import { UserService } from '../../services/user.service';
import { MessageService } from '../../services/message.service';
import { Me } from '../../models/me.model';


@Component({
    selector: "stadistics",
    templateUrl: './stadistics.component.html',
    styleUrl: './stadistics.component.css'
})
export class StadisticsComponent implements AfterViewInit{

    constructor(
        private service: StadisticsService,
        private userService: UserService,
        private messageService: MessageService) {
        this.checkUser();
    }

    pieChart: Map<String, number>;
    trust: number;
    me: Me;

    // Atributes
    reliability: boolean;
    effort: boolean;
    communication: boolean;
    attitude: boolean;
    problemsResolution: boolean;
    leadership: boolean;

    // PieChart parameters
    title: string = 'Aptitudes de los socorristas';
    type: ChartType = ChartType["PieChart"];
    width: number = 550;
    height: number = 400;
    data: (string | number)[][] = [
        ['Confianza', 0],
        ['Esfuerzo', 0],
        ['Comunicación', 0],
        ['Actitud positiva', 0],
        ['Resolucion', 0],
        ['Liderazgo', 0]
    ];
    options = {
        TitletextSytle: {
            fontsize: 18
        },
        legend: {
            textStyle: {
                fontsize: 18
            }
        },
        tooltip: {
            textStyle: {
              fontSize: 18
            },
            showColorCode: true,
            isHtml: true
        },
        pieSliceTextStyle: {
            fontSize: 18
        },
    }

    ngAfterViewInit() {
        // Escucha el evento de gráfico listo
        const chartWrapper = document.getElementById('chart-wrapper');
        if (chartWrapper)
        chartWrapper.addEventListener('mouseover', () => {
          const tooltips = document.querySelectorAll('div.google-visualization-tooltip');
          tooltips.forEach(tooltip => {
            tooltip.classList.add('custom-tooltip');
          });
        });
      }

    ngOnInit(): void {
        this.service.getPieChart().subscribe(
            response => {
                let aux1 = response as any
                this.pieChart = new Map<String, number>;
                for (const key in aux1) {
                    if (response.hasOwnProperty(key)) {
                        this.pieChart.set(key, aux1[key]);
                    }
                }

                this.data = [
                    ['Confianza', this.pieChart.get("Confianza") || 0],
                    ['Esfuerzo', this.pieChart.get("Esfuerzo") || 0],
                    ['Comunicación', this.pieChart.get("Comunicación") || 0],
                    ['Actitud positiva', this.pieChart.get("Actitud positiva") || 0],
                    ['Resolucion', this.pieChart.get("Resolución de problemas") || 0],
                    ['Liderazgo', this.pieChart.get("Liderazgo") || 0]
                ];
            },
            error => {
                console.error(error)
            }
        );
    }

    checkUser() {
        this.userService.me().subscribe(
            response => {
                this.me = response as Me;
                if (this.me.mail != "admin")  // The check is done by mail, not by role
                    this.messageService.showFatalError("Solo los administradores pueden acceder a esta página")
            },
            _error =>
                this.messageService.showFatalError("Solo los administradores pueden acceder a esta página")
        );
    }
}