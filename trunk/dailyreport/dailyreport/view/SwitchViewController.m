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
@synthesize shareView = _shareView;
@synthesize backgroundShareView = _backgroundShareView;
@synthesize preScreenShot = _preScreenShot;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    
    [super loadView];
    self.title = self.brand;
    UIBarButtonItem* backButton = [[[UIBarButtonItem alloc] initWithTitle:@"\U000025C0\U0000FE0E" style:UIBarButtonItemStylePlain target:self action:@selector(backAction)] autorelease];

    [self.navigationItem setLeftBarButtonItem:backButton animated:YES];
    
    CGRect frame = [[UIScreen mainScreen] bounds];
    NSLog(@"%f",frame.size.height);
    self.view.backgroundColor = [UIColor whiteColor];
    int offset = 0;
    if(DEVICE_VERSION<7.0){
        offset= 64;
    }
    self.contentView = [[[SwipeScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-offset-30)] autorelease];
    self.contentView.index = self.index;
    self.contentView.brand = self.brand;
    self.contentView.backgroundColor = [UIColor whiteColor];
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
    [shareButton addTarget:self action:@selector(showShareView) forControlEvents:UIControlEventTouchUpInside];
    
    y = frame.size.height-200-offset;
    self.backgroundShareView =[[UIView alloc] initWithFrame:frame];
    self.shareView = [[UIView alloc] initWithFrame:CGRectMake(0, y, 320, 200)];
    self.shareView.backgroundColor = [StyleSheet colorFromHexString:@"#edeef0"];

    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    cancelButton.frame =CGRectMake(50, 150, 220, 40);
    UIImage* cancelImage= [UIImage imageNamed:@"分享取消按钮.png"];
    [cancelButton setImage:cancelImage forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(hideShareView) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton* shareSessionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    shareSessionButton.frame =CGRectMake(40, 30, 80, 80);
    UIImage* shareSessionImage= [UIImage imageNamed:@"图标-微信好友.png"];
    [shareSessionButton setImage:shareSessionImage forState:UIControlStateNormal];
    [shareSessionButton addTarget:self action:@selector(sendSessionImageContent) forControlEvents:UIControlEventTouchUpInside];
    UILabel* shareSessionLabel = [self createLabel:@"微信好友" frame:CGRectMake(40, 110, 80, 20) textColor:@"#849484" font:14 backgroundColor:nil textAlignment:NSTextAlignmentCenter];

    UIButton* shareTimelineButton = [UIButton buttonWithType:UIButtonTypeCustom];
    shareTimelineButton.frame =CGRectMake(200, 30, 80, 80);
    UIImage* shareTimelineImage= [UIImage imageNamed:@"图标-微信朋友圈.png"];
    [shareTimelineButton setImage:shareTimelineImage forState:UIControlStateNormal];
    [shareTimelineButton addTarget:self action:@selector(sendTimelineImageContent) forControlEvents:UIControlEventTouchUpInside];
    UILabel* shareTimelineLabel = [self createLabel:@"微信朋友圈" frame:CGRectMake(200, 110, 80, 20) textColor:@"#849484" font:14 backgroundColor:nil textAlignment:NSTextAlignmentCenter];
    [self.shareView addSubview:cancelButton];

    [self.shareView addSubview:shareSessionButton];
    [self.shareView addSubview:shareTimelineButton];
    [self.shareView addSubview:shareSessionLabel];
    [self.shareView addSubview:shareTimelineLabel];
    [self.shareView addSubview:cancelButton];
    [self.backgroundShareView addSubview:self.shareView];
    [self.view addSubview:shareButton];

}
- (void)viewDidLoad
{
    [super viewDidLoad];
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
    UIGraphicsBeginImageContext(self.contentView.contentSize);
    CGPoint savedContentOffset = self.contentView.contentOffset;
    CGRect savedFrame = self.contentView.frame;
    self.contentView.contentOffset = CGPointZero;
    self.contentView.frame = CGRectMake(0,0, self.contentView.contentSize.width, self.contentView.contentSize.height);
    [self.contentView.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage* viewImage = UIGraphicsGetImageFromCurrentImageContext();
    self.contentView.contentOffset = savedContentOffset;
    self.contentView.frame = savedFrame;
    UIGraphicsEndImageContext();
    return viewImage;
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

-(void) hideShareView{
    [UIView animateWithDuration:0.25f animations:^{
        self.shareView.alpha = 0;
        [self.shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self.backgroundShareView removeFromSuperview];
    }];
}
-(void) showShareView{
    if(![WXApi isWXAppInstalled]){
        TTAlertNoTitle(@"本设备还未安装微信,无法使用微信分享功能");
    }else{
        self.preScreenShot =[self screenShot];
        [self.view addSubview:self.backgroundShareView];
        self.shareView.alpha = 0;
        self.backgroundShareView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
        [UIView animateWithDuration:0.25f animations:^{
            self.shareView.alpha = 1;
            [self.shareView layoutIfNeeded];
        }];
    }
    
}
- (void) sendSessionImageContent
{
    [self hideShareView];
    WXMediaMessage *message = [WXMediaMessage message];
    UIImage* screenShot = self.preScreenShot;
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
- (void) sendTimelineImageContent
{
    [self hideShareView];
    WXMediaMessage *message = [WXMediaMessage message];
    UIImage* screenShot =self.preScreenShot;
    UIImage *thumbImage = [self imageWithImage:screenShot scaledToSize:CGSizeMake(32,64)];
    [message setThumbImage:thumbImage];
    
    WXImageObject *ext = [WXImageObject object];
    ext.imageData = UIImagePNGRepresentation(screenShot);
    NSLog(@"%i",ext.imageData.length/1024);
    message.mediaObject = ext;
    
    SendMessageToWXReq* req = [[[SendMessageToWXReq alloc] init]autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneTimeline;
    
    [WXApi sendReq:req];
}
- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_contentView);
    TT_RELEASE_SAFELY(_shareView);
    TT_RELEASE_SAFELY(_backgroundShareView);
    TT_RELEASE_SAFELY(_preScreenShot);
    [super dealloc];
}

@end
