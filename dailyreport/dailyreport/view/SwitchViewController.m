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
#import "AppDelegate.h"
@implementation SwitchViewController

@synthesize contentView = _contentView;
@synthesize brand = _brand;
@synthesize index = _index;
@synthesize shareView = _shareView;
@synthesize backgroundShareView = _backgroundShareView;
@synthesize preScreenShot = _preScreenShot;
@synthesize selectedDate = _selectedDate;
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
    if(DEVICE_VERSION<6.0){
        backButton.title = @"<";
    }
    [self.navigationItem setLeftBarButtonItem:backButton animated:YES];
    
    UIButton* shareButtonImage = [UIButton buttonWithType:UIButtonTypeCustom];
    shareButtonImage.frame =CGRectMake(0, 0, 40, 40);
    [shareButtonImage setImage:[UIImage imageNamed:@"图标-分享.png"] forState:UIControlStateNormal];
    [shareButtonImage addTarget:self action:@selector(showShareView:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem* shareButton = [[[UIBarButtonItem alloc] initWithCustomView:shareButtonImage] autorelease];
    [self.navigationItem setRightBarButtonItem:shareButton animated:YES];
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
    
    
    y = frame.size.height-200-offset;
    self.backgroundShareView =[[UIView alloc] initWithFrame:frame];
    self.shareView = [[UIView alloc] initWithFrame:CGRectMake(0, y, 320, 200)];
    self.shareView.backgroundColor = [StyleSheet colorFromHexString:@"#edeef0"];
    
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    cancelButton.frame =CGRectMake(50, 150, 220, 40);
    UIImage* cancelImage= [UIImage imageNamed:@"分享取消按钮.png"];
    [cancelButton setImage:cancelImage forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(hideShareView) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton* wxButton = [UIButton buttonWithType:UIButtonTypeCustom];
    wxButton.frame =CGRectMake(120, 30, 80, 80);
    UIImage* wxImage= [UIImage imageNamed:@"图标-微信好友.png"];
    [wxButton setImage:wxImage forState:UIControlStateNormal];
    [wxButton addTarget:self action:@selector(sendWXContent) forControlEvents:UIControlEventTouchUpInside];
    UILabel* wxLabel = [self createLabel:@"微信好友" frame:CGRectMake(120, 110, 80, 20) textColor:@"#849484" font:14 backgroundColor:nil textAlignment:ALIGN_CENTER];
    [self.shareView addSubview:cancelButton];
    
    [self.shareView addSubview:wxButton];
    [self.shareView addSubview:wxLabel];
    [self.shareView addSubview:cancelButton];
    [self.backgroundShareView addSubview:self.shareView];
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
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        self.navigationController.navigationBar.barTintColor=[UIColor blackColor];
    }else{
        self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    }
    #else
        self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    #endif
    
    UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:18];
    label.textAlignment = [self getAlignment:ALIGN_CENTER];
    label.textColor=[UIColor whiteColor];
    label.text = self.brand;
    self.navigationItem.titleView = label;
    [label sizeToFit];
    [self.contentView setContentOffset:CGPointZero animated:NO];
    if(self.selectedDate!=nil&&![self.selectedDate isEqualToDate:DrAppDelegate.user.date]){
        NSLog(@"当前页面时间和选择的时间不同，重新加载该页面数据");
        [self changeDateAndReload];
    }
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
-(UILabel*) createDecimalLabel:(NSNumber*) number
                          unit:(NSString*) unit frame:(CGRect)  frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    
    label.text = [NSString stringWithFormat:@"%@ %@",[formatter stringFromNumber:number],unit];
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
-(UILabel*) createDecimalLabel:(NSNumber*) number frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    
    label.text = [formatter stringFromNumber:number];
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

-(UIImage*)screenShot{
    
    CGSize imageSize = [[UIScreen mainScreen] bounds].size;
    if (NULL != UIGraphicsBeginImageContextWithOptions)
        UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0);
    else
        UIGraphicsBeginImageContext(imageSize);
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    // Iterate over every window from back to front
    for (UIWindow *window in [[UIApplication sharedApplication] windows])
    {
        if (![window respondsToSelector:@selector(screen)] || [window screen] == [UIScreen mainScreen])
        {
            // -renderInContext: renders in the coordinate space of the layer,
            // so we must first apply the layer's geometry to the graphics context
            CGContextSaveGState(context);
            // Center the context around the window's anchor point
            CGContextTranslateCTM(context, [window center].x, [window center].y);
            // Apply the window's transform about the anchor point
            CGContextConcatCTM(context, [window transform]);
            // Offset by the portion of the bounds left of and above the anchor point
            CGContextTranslateCTM(context,
                                  -[window bounds].size.width * [[window layer] anchorPoint].x,
                                  -[window bounds].size.height * [[window layer] anchorPoint].y);
            
            // Render the layer hierarchy to the current context
            [[window layer] renderInContext:context];
            
            // Restore the context
            CGContextRestoreGState(context);
        }
    }
    
    // Retrieve the screenshot image
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    
    return image;
//    UIGraphicsBeginImageContext(self.contentView.contentSize);
//    CGPoint savedContentOffset = self.contentView.contentOffset;
//    CGRect savedFrame = self.contentView.frame;
//    self.contentView.contentOffset = CGPointZero;
//    self.contentView.frame = CGRectMake(0,0, self.contentView.contentSize.width, self.contentView.contentSize.height);
//    [self.contentView.layer renderInContext:UIGraphicsGetCurrentContext()];
//    UIImage* viewImage = UIGraphicsGetImageFromCurrentImageContext();
//    self.contentView.contentOffset = savedContentOffset;
//    self.contentView.frame = savedFrame;
//    UIGraphicsEndImageContext();
//    return viewImage;
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
-(void)openCalendar:(id)sender{
    UIButton* b = (UIButton*)sender;
    PMCalendarController* pmCC = [[[PMCalendarController alloc] initWithDate:self.selectedDate] autorelease];
    pmCC.delegate = self;
    pmCC.allowsPeriodSelection = NO;
    pmCC.mondayFirstDayOfWeek = YES;
   
    [pmCC presentCalendarFromView:b
         permittedArrowDirections:PMCalendarArrowDirectionAny
                         animated:YES];
}

#pragma mark PMCalendarControllerDelegate methods

- (void)calendarController:(PMCalendarController *)calendarController didChangePeriod:(PMPeriod *)newPeriod
{
    NSDate* date = newPeriod.startDate;
    DrAppDelegate.user.date = date;
    [calendarController dismissCalendarAnimated:YES];
    [self changeDateAndReload];
    
}
-(void)changeDateAndReload{
    for(UIView* view in [self.contentView subviews]){
        [view removeFromSuperview];
    }
}
-(void) showShareView:(id)sender{
    
    
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

- (void) sendWXContent
{
    [self hideShareView];
    WXMediaMessage *message = [WXMediaMessage message];
    UIImage* screenShot =self.preScreenShot;
    UIImage *thumbImage = [self imageWithImage:screenShot scaledToSize:CGSizeMake(32,64)];
    [message setThumbImage:thumbImage];
    
    UIImage* resizedScreenShot = screenShot;
    CGRect frame = [[UIScreen mainScreen] bounds];
    resizedScreenShot =[self imageWithImage:resizedScreenShot scaledToSize:CGSizeMake(320,frame.size.height)];
    WXImageObject *ext = [WXImageObject object];
    ext.imageData = UIImagePNGRepresentation(resizedScreenShot);
    NSLog(@"%i",ext.imageData.length/1024);
    message.mediaObject = ext;
    message.title = @"PB日报";
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
    TT_RELEASE_SAFELY(_shareView);
    TT_RELEASE_SAFELY(_backgroundShareView);
    TT_RELEASE_SAFELY(_preScreenShot);
    TT_RELEASE_SAFELY(_selectedDate);
    [super dealloc];
}

@end
