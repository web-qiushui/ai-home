var gulp = require('gulp');
var cache = require('gulp-cache');
var imageMin = require('gulp-imagemin');
gulp.task('image', function () {
    gulp.src('src/static/*.*')
        .pipe(cache(
            imageMin({
                optimizationLevel: 5, //类型：Number  默认：3  取值范围：0-7（优化等		级）
                progressive      : true, //类型：Boolean 默认：false 无损压缩jpg图片
                interlaced       : true, //类型：Boolean 默认：false 隔行扫描gif进行渲染
                multipass        : true //类型：Boolean 默认：false 多次优化svg直到完全优化
            })
        ))
        .pipe(gulp.dest('dist/mp-weixin/static'))
})
