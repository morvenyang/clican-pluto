//
//  SwitchViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "StyleSheet.h"
#import "WXApi.h"

@implementation SwitchViewController

@synthesize contentView = _contentView;
@synthesize brand = _brand;
@synthesize index = _index;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void)handleSwipeFrom:(UISwipeGestureRecognizer*)recognize{
    NSLog(@"switch left or right");
    if(recognize.direction ==UISwipeGestureRecognizerDirectionLeft){
        if(self.index==1){
            NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==2){
            NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==3){
            NSString* url = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }
    }
    
    if(recognize.direction ==UISwipeGestureRecognizerDirectionRight){
        if(self.index==2){
            NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==3){
            NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==4){
            NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }
    }
}

- (void)loadView
{
    
    [super loadView];
    self.title = self.brand;
    UIButton* backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [backButton setTitle:@"<" forState:UIControlStateNormal];
    
    [backButton addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    //backButton.frame = CGRectMake(35, 0, 15, 20);
    [backButton sizeToFit];
    
    
    // create button item -- possible because UIButton subclasses UIView!
    UIBarButtonItem* backItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [self.navigationItem setLeftBarButtonItem:backItem animated:YES];
    
    CGRect frame = [[UIScreen mainScreen] bounds];
    NSLog(@"%f",frame.size.height);
    self.view.backgroundColor = [UIColor whiteColor];
    int offset = 0;
    if(DEVICE_VERSION<7.0){
        offset= 64;
    }
    self.contentView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-offset-30)] autorelease];
    
    self.contentView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.contentView];
    
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];

    CGFloat y = frame.size.height-30-offset;
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake(130, y, 60, 30)] autorelease];
    paginationView.backgroundColor = [UIColor whiteColor];
    for(int i=1;i<=4;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(15*(i-1), 10, 6, 6)] autorelease];
        if(i==self.index){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [paginationView addSubview:v];
    }
    [self.view addSubview:paginationView];
    UIButton* shareButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* shareImage= [UIImage imageNamed:@"图标-分享.png"];
    [shareButton setImage:shareImage forState:UIControlStateNormal];
    shareButton.frame = CGRectMake(280, y, 30, 30);
    [shareButton addTarget:self action:@selector(sendImageContent) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:shareButton];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    UISwipeGestureRecognizer* recognizer;
    recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
    
    [recognizer setDirection:(UISwipeGestureRecognizerDirectionLeft)];
    [[self view] addGestureRecognizer:recognizer];
    [recognizer release];
    
    recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
    
    [recognizer setDirection:(UISwipeGestureRecognizerDirectionRight)];
    [[self view] addGestureRecognizer:recognizer];
    [recognizer release];
    // Do any additional setup after loading the view.
}

-(void) backAction{
    TTOpenURL(@"peacebird://index");
}

- (void)viewWillAppear:(BOOL)animated {
    if(DEVICE_VERSION>=7.0){
        self.navigationController.navigationBar.barTintColor=[UIColor blackColor];
    }else{
        self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    }
    
    UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:18];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor=[UIColor whiteColor];
    label.text = self.brand;
    self.navigationItem.titleView = label;
    [label sizeToFit];
    
    [super viewWillAppear:animated];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame{
    UIImage* image =[UIImage imageNamed:imageName];
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:frame];
    
    imageView.image = image;
    return imageView;
}

-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    label.text = text;
    label.font = [UIFont systemFontOfSize:font];
    label.textColor = [StyleSheet colorFromHexString:textColor];
    if(backgroundColor==nil){
        label.backgroundColor = [UIColor clearColor];
    }else{
        label.backgroundColor = [StyleSheet colorFromHexString:backgroundColor];
    }
    
    return label;
}

-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    label.text = text;
    label.font = [UIFont systemFontOfSize:font];
    label.textColor = [StyleSheet colorFromHexString:textColor];
    label.textAlignment =textAlignment;
    if(backgroundColor==nil){
        label.backgroundColor = [UIColor clearColor];
    }else{
        label.backgroundColor = [StyleSheet colorFromHexString:backgroundColor];
    }
    
    return label;
}

-(UIImage*)screenShot{
    CGImageRef UIGetScreenImage();
    CGImageRef img = UIGetScreenImage();
    UIImage* saveImage= [UIImage imageWithCGImage:img];
    return saveImage;
}

- (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize {
    //UIGraphicsBeginImageContext(newSize);
    // In next line, pass 0.0 to use the current device's pixel scaling factor (and thus account for Retina resolution).
    // Pass 1.0 to force exact pixel size.
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}

- (void) sendImageContent
{
    WXMediaMessage *message = [WXMediaMessage message];
    UIImage* screenShot =[self screenShot];
    UIImage *thumbImage = [self imageWithImage:screenShot scaledToSize:CGSizeMake(32,64)];
    [message setThumbImage:thumbImage];
    
    WXImageObject *ext = [WXImageObject object];
    ext.imageData = UIImagePNGRepresentation(screenShot);
    NSLog(@"%i",ext.imageData.length/1024);
    message.mediaObject = ext;
    
    SendMessageToWXReq* req = [[[SendMessageToWXReq alloc] init]autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;
    
    [WXApi sendReq:req];
}

- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_contentView);
    [super dealloc];
}

@end
