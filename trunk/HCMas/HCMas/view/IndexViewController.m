//
//  IndexViewController.m
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "IndexViewController.h"
#import "StyleSheet.h"
#import "Constants.h"
@implementation IndexViewController
@synthesize imageIndex = _imageIndex;
@synthesize pointImageViews = _pointImageViews;
@synthesize topImageView = _topImageView;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.imageIndex = 1;
        self.pointImageViews = [NSMutableArray array];
    }
    return self;
}
-(void)loadView{
    [super loadView];
    self.navigationController.navigationBarHidden = YES;
    self.view.backgroundColor = [UIColor whiteColor];
    CGRect frame = [[UIScreen mainScreen] bounds];
    [self.view addSubview:[self createImageViewFromNamedImage:@"main_background.png" frame:CGRectMake(0, 22, 320, frame.size.height-22)]];
    [self.view addSubview:[self createImageViewFromNamedImage:@"title_logo_v15.png" frame:CGRectMake(2, 24, 24, 26)]];
    [self.view addSubview:[self createLabel:@"华测自动化检测与预警系统" frame:CGRectMake(27, 20, 200, 30) textColor:@"#ffffff" font:15 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    [self.view addSubview:[self createImageViewFromNamedImage:@"clear.png" frame:CGRectMake(290, 24, 26, 26)]];
    self.topImageView = [self createImageViewFromNamedImage:@"default_pic_1.jpg" frame:CGRectMake(0, 50, 320, 130)];
    [self.view addSubview:self.topImageView];
    
    UISwipeGestureRecognizer* swipeGestureRight = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(switchImage:)] autorelease];
    swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    
    UISwipeGestureRecognizer* swipeGestureLeft = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(switchImage:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    self.topImageView.userInteractionEnabled =YES;
    [self.topImageView addGestureRecognizer:swipeGestureLeft];
    [self.topImageView addGestureRecognizer:swipeGestureRight];
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyy年MM月dd日 EEEE"];
    
    [self.view addSubview:[self createLabel:[dateFormatter stringFromDate:[NSDate date]] frame:CGRectMake(10, 180, 150, 20) textColor:@"#ffffff" font:12 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    
    [self.view addSubview:[self createPaginationView:180]];
}

    
-(void)switchImage:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        if(self.imageIndex<4){
            self.imageIndex=self.imageIndex+1;
        }
    }else if(direction==UISwipeGestureRecognizerDirectionRight){
        if(self.imageIndex>1){
            self.imageIndex=self.imageIndex-1;
        }
    }
    self.topImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"default_pic_%i.jpg",self.imageIndex]];
    int i = 1;
    UIImage* lightImage= [UIImage imageNamed:@"img_ratio.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"img_ratio_selected.png"];
    for(UIImageView* view in self.pointImageViews){
        if(i==self.imageIndex){
            view.image =highLightImage;
        }else{
            view.image =lightImage;
        }
        i++;
    }
}
-(UIView*) createPaginationView:(int)y{
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake(250, y, 16*4, 20)] autorelease];
    UIImage* lightImage= [UIImage imageNamed:@"img_ratio.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"img_ratio_selected.png"];
    paginationView.backgroundColor = [UIColor clearColor];
    for(int i=1;i<=4;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(16*(i-1), 5, 6, 6)] autorelease];
        if(i==self.imageIndex){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [self.pointImageViews addObject:v];
        [paginationView addSubview:v];
    }
    return paginationView;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#ifdef __IPHONE_6_0
-(NSTextAlignment) getAlignment:(int)textAlignment{
    if(textAlignment==ALIGN_LEFT){
        return NSTextAlignmentLeft;
    }else if(textAlignment==ALIGN_CENTER){
        return NSTextAlignmentCenter;
    }else{
        return NSTextAlignmentRight;
    }
}
#else
-(UITextAlignment) getAlignment:(int)textAlignment{
    if(textAlignment==ALIGN_LEFT){
        return UITextAlignmentLeft;
    }else if(textAlignment==ALIGN_CENTER){
        return UITextAlignmentCenter;
    }else{
        return UITextAlignmentRight;
    }
}
#endif
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    label.text = text;
    label.font = [UIFont systemFontOfSize:font];
    label.textColor = [StyleSheet colorFromHexString:textColor];
    label.textAlignment =[self getAlignment:textAlignment];
    if(backgroundColor==nil){
        label.backgroundColor = [UIColor clearColor];
    }else{
        label.backgroundColor = [StyleSheet colorFromHexString:backgroundColor];
    }
    
    return label;
}

-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame{
    UIImage* image =[UIImage imageNamed:imageName];
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:frame];
    
    imageView.image = image;
    return imageView;
}

- (UIImage *) createImageWithColor: (UIColor *) color
{
    CGRect rect=CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame{
    UIImage* image =[self createImageWithColor:color];
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:frame];
    
    imageView.image = image;
    return imageView;
}

@end
