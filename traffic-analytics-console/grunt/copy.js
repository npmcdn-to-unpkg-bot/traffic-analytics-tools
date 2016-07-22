module.exports = {
    dist:{
        files: [
            {
                src: [
                    'index.html',
                    'tpl/**',
                    'assets/fonts/**',
                    'assets/img/**',
                    'assets/l10n/**'
                ],
                dest: 'dist/',
                cwd:  'src/',
                expand: true
            },
            { src: '**', cwd: 'bower_components/bootstrap/dist/fonts', dest: 'dist/assets/fonts', expand: true },
            { src: '**', cwd: 'bower_components/font-awesome/fonts', dest: 'dist/assets/fonts', expand: true }
        ]
    },
    html: {
        files: [
            {expand: true, src: '**', cwd:'src/fonts/', dest: 'html/fonts/'},
            {expand: true, src: "**", cwd: 'src/api',     dest: "html/api"},
            {expand: true, src: '**', cwd:'src/img/', dest: 'html/img/'},
            {expand: true, src: '*.css', cwd:'src/css/', dest: 'html/css/'},
            {expand: true, src: '**', cwd:'swig/js/', dest: 'html/js/'}
        ]
    },
    landing: {
        files: [
            {expand: true, src:'**', cwd:'src/fonts/', dest: 'landing/fonts/'},
            {expand: true, src:'*.css', cwd:'src/css/', dest: 'landing/css/'},
            {src:'html/css/app.min.css', dest: 'landing/css/app.min.css'}
        ]
    }

};
