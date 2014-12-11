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
@synthesize calendarLabel = _calendarLabel;
@synthesize direction = _direction;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _yOffset= 64.0;
        self.direction = @"left";

    }
    return self;
}

- (void)loadView
{
    
    [super loadView];
    #ifdef __IPHONE_7_0
    self.edgesForExtendedLayout = UIRectEdgeNone;
    #endif
    UIButton* backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [backButton setImage:[UIImage imageNamed:@"back.png"] forState:UIControlStateNormal];
    
    backButton.frame =CGRectMake(10, 0, 40, 40);
    
    [backButton addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        backButton.contentEdgeInsets=UIEdgeInsetsMake(0, -30, 0, 0);
    }
    #endif
    #ifdef __IPHONE_6_0
        if(DEVICE_VERSION<7.0){
            backButton.contentEdgeInsets=UIEdgeInsetsMake(0, -10, 0, 0);
        }
    #endif

    UIBarButtonItem* backButtonItem = [[[UIBarButtonItem alloc] initWithCustomView:backButton] autorelease];

    [self.navigationItem setLeftBarButtonItem:backButtonItem animated:YES];
    
    
    UIButton* shareButtonImage = [UIButton buttonWithType:UIButtonTypeCustom];
    shareButtonImage.frame =CGRectMake(SCREEN_WIDTH-50, 0, 40, 40);
    [shareButtonImage setImage:[UIImage imageNamed:@"图标-分享"] forState:UIControlStateNormal];
    [shareButtonImage addTarget:self action:@selector(showShareView:) forControlEvents:UIControlEventTouchUpInside];

    
    UIBarButtonItem* shareButton = [[[UIBarButtonItem alloc] initWithCustomView:shareButtonImage] autorelease];
    if(self.index!=-2){
        [self.navigationItem setRightBarButtonItem:shareButton animated:YES];
    }

    CGRect frame = [[UIScreen mainScreen] bounds];
    NSLog(@"%f",frame.size.height);
    self.view.backgroundColor = [UIColor whiteColor];

    if(self.index!=-2){
        self.contentView = [[[SwipeScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-_yOffset-30)] autorelease];
    }else{
        self.contentView = [[[SwipeScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-_yOffset)] autorelease];
    }
    //self.contentView.previous = self;
    NSLog(@"%f",self.contentView.frame.size.height);
    self.contentView.index = self.index;
    self.contentView.brand = self.brand;
    self.contentView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.contentView];
    

    
    CGFloat y = SCREEN_HEIGHT-30-_yOffset;
    if(self.index!=-2){
        [self.view addSubview:[self createPaginationView:y]];
    }
    
    
    
    y = SCREEN_HEIGHT-200*SCREEN_WIDTH/320-_yOffset;
    self.backgroundShareView =[[[UIView alloc] initWithFrame:frame] autorelease];
    self.shareView = [[[UIView alloc] initWithFrame:CGRectMake(0, y, SCREEN_WIDTH, 200*SCREEN_WIDTH/320)] autorelease];
    self.shareView.backgroundColor = [StyleSheet colorFromHexString:@"#edeef0"];
    
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage* cancelImage= [UIImage imageNamed:@"分享取消按钮"];
    cancelButton.frame =CGRectMake((SCREEN_WIDTH-cancelImage.size.width)/2, self.shareView.frame.size.height-cancelImage.size.height-20, cancelImage.size.width, cancelImage.size.height);
    [cancelButton setImage:cancelImage forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(hideShareView) forControlEvents:UIControlEventTouchUpInside];
    int labelFont = [self getFont:14 ip6Offset:2 ip6pOffset:2];
    UIButton* switchButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* switchImage= [UIImage imageNamed:@"图标-切换报表"];
    CGFloat space =(SCREEN_WIDTH-switchImage.size.width*3)/6;
    CGFloat wOffset = 0;
    switchButton.frame =CGRectMake(space, 30, switchImage.size.width,switchImage.size.height);
    [switchButton setImage:switchImage forState:UIControlStateNormal];
    [switchButton addTarget:self action:@selector(openPBNavigationView) forControlEvents:UIControlEventTouchUpInside];
    UILabel* switchLabel = [self createLabel:@"切换报表" frame:CGRectMake(space-20, 40+switchImage.size.height, switchImage.size.width+40, 20*SCREEN_WIDTH/320) textColor:@"#849484" font:labelFont backgroundColor:nil textAlignment:ALIGN_CENTER];
    wOffset+=switchImage.size.width+space*3;
    
    UIButton* mailButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* mailImage= [UIImage imageNamed:@"图标-邮件分享"];
    mailButton.frame =CGRectMake(wOffset, 30, mailImage.size.width, mailImage.size.height);
    [mailButton setImage:mailImage forState:UIControlStateNormal];
    [mailButton addTarget:self action:@selector(sendMailContent) forControlEvents:UIControlEventTouchUpInside];
    UILabel* mailLabel = [self createLabel:@"邮件分享" frame:CGRectMake(wOffset-20, 40+mailImage.size.height, mailImage.size.width+40, 20*SCREEN_WIDTH/320) textColor:@"#849484" font:labelFont backgroundColor:nil textAlignment:ALIGN_CENTER];
    wOffset+=mailImage.size.width+space*2;
    
    UIButton* wxButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* wxImage= [UIImage imageNamed:@"图标-微信好友"];
    wxButton.frame =CGRectMake(wOffset, 30, wxImage.size.width, wxImage.size.height);
    [wxButton setImage:wxImage forState:UIControlStateNormal];
    [wxButton addTarget:self action:@selector(sendWXContent) forControlEvents:UIControlEventTouchUpInside];
    UILabel* wxLabel = [self createLabel:@"微信好友" frame:CGRectMake(wOffset-20, 40+wxImage.size.height, wxImage.size.width+40, 20*SCREEN_WIDTH/320) textColor:@"#849484" font:labelFont backgroundColor:nil textAlignment:ALIGN_CENTER];

    
    [self.shareView addSubview:cancelButton];
    [self.shareView addSubview:switchButton];
    [self.shareView addSubview:switchLabel];
    [self.shareView addSubview:mailButton];
    [self.shareView addSubview:mailLabel];
    [self.shareView addSubview:wxButton];
    [self.shareView addSubview:wxLabel];
    [self.shareView addSubview:cancelButton];
    [self.backgroundShareView addSubview:self.shareView];
    
}
-(UIView*) createPaginationView:(int)y{
    int c = 8;
    if([self.brand isEqualToString:@"电商"]){
        c=2;
    }
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2-16*c/2, y, 16*c, 30)] autorelease];
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    paginationView.backgroundColor = [UIColor whiteColor];
    for(int i=1;i<=c;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(16*(i-1), 10, 6, 6)] autorelease];
        if(i==self.index){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [paginationView addSubview:v];
    }
    return paginationView;
}


-(void) backAction{
    [[TTNavigator navigator] removeAllViewControllers];
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
    

    [super viewWillAppear:animated];
    [self turnpage];
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:NO];
}
-(void)viewDidLoad
{
    [super viewDidLoad];
    
}
-(void)turnpage{
    CABasicAnimation *animation = [CABasicAnimation  animationWithKeyPath:@"position"];
    if([self.direction isEqualToString:@"left"]){
        animation.fromValue = [NSValue valueWithCGPoint:CGPointMake(1.5*SCREEN_WIDTH, SCREEN_HEIGHT/2+20)];
    }else{
        animation.fromValue = [NSValue valueWithCGPoint:CGPointMake(-SCREEN_WIDTH/2, SCREEN_HEIGHT/2+20)];
    }
    

    animation.duration =0.5;
    animation.cumulative =NO;
    animation.repeatCount=1;
    
    [self.view.layer addAnimation:animation forKey:@"animation"];

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
    UIImageView* imageView = [[[UIImageView alloc] initWithFrame:frame] autorelease];
    
    imageView.image = image;
    return imageView;
}

-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName point:(CGPoint) point{
    UIImage* image =[UIImage imageNamed:imageName];
    UIImageView* imageView = [[[UIImageView alloc] initWithFrame:CGRectMake(point.x, point.y, image.size.width, image.size.height)] autorelease];
    
    imageView.image = image;
    return imageView;
}

-(UIImageView*) createImageViewFromImage:(UIImage*) image frame:(CGRect) frame{
    UIImageView* imageView = [[[UIImageView alloc] initWithFrame:frame] autorelease];
    
    imageView.image = image;
    return imageView;
}
-(UIView*) createDailyView:(NSString*) iconName label:(NSString*)label{
    int labelFontSize = [self getFont:12 ip6Offset:2 ip6pOffset:4];
    UIImage* iconImage = [UIImage imageNamed:iconName];
    CGFloat dailyViewHeight =iconImage.size.height;
    if(IS_IPHONE6||IS_IPHONE6_PLUS){
        dailyViewHeight =dailyViewHeight*1.3;
    }
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, dailyViewHeight)] autorelease];
    dailyView.backgroundColor =[StyleSheet colorFromHexString:[[[NSBundle mainBundle] infoDictionary] objectForKey:[NSString stringWithFormat:@"%@背景",self.brand]]];
    UIImageView* retailImageView = [self createImageViewFromImage:iconImage frame:CGRectMake(0, (dailyViewHeight-iconImage.size.height)/2, iconImage.size.width, iconImage.size.height)];
    UILabel* retailLabel = [self createLabel:label frame:CGRectMake(40, (dailyViewHeight-iconImage.size.height)/2, 120, iconImage.size.height) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    NSString* dateStr = @"";
    if(self.selectedDate!=nil){
        dateStr =[dateFormatter stringFromDate:self.selectedDate];
    }
    self.calendarLabel = [self createLabel:dateStr frame:CGRectMake(SCREEN_WIDTH/2, (dailyViewHeight-iconImage.size.height)/2, SCREEN_WIDTH/2-30, iconImage.size.height) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    self.calendarLabel.textAlignment = NSTextAlignmentRight;
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:self.calendarLabel];
    return dailyView;
}

-(CGFloat) getTabHeight{
    if (IS_IPHONE4||IS_IPHONE5) {
        return 50;
    }else if(IS_IPHONE6){
        return 45;
    }else{
        return 45;
    }
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
    if(number.doubleValue>-1&&number.doubleValue<1&&number.doubleValue!=0){
        [formatter setMaximumFractionDigits:2];
        [formatter setMinimumFractionDigits:2];
    }else{
        [formatter setMaximumFractionDigits:0];
        [formatter setMinimumFractionDigits:0];
    }
    label.text = [NSString stringWithFormat:@"%@%@",[formatter stringFromNumber:number],unit];
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
    if(number.doubleValue>-1&&number.doubleValue<1&&number.doubleValue!=0){
        [formatter setMaximumFractionDigits:2];
        [formatter setMinimumFractionDigits:2];
    }else{
        [formatter setMaximumFractionDigits:0];
        [formatter setMinimumFractionDigits:0];
    }
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
    self.preScreenShot =[self screenShot];
    [self.view addSubview:self.backgroundShareView];
    self.shareView.alpha = 0;
    self.backgroundShareView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
        [UIView animateWithDuration:0.25f animations:^{
            self.shareView.alpha = 1;
            [self.shareView layoutIfNeeded];
    }];
}
- (void) sendMailContent{
    if ([MFMailComposeViewController canSendMail]) {
        MFMailComposeViewController* mailController = [[[MFMailComposeViewController alloc] init] autorelease];
        mailController.mailComposeDelegate = self;
        [mailController setSubject:@"PB日报"];
        
        
        [mailController addAttachmentData:UIImagePNGRepresentation(self.preScreenShot) mimeType:@"image/png" fileName:@"PB日报.png"];
        [self presentViewController:mailController animated:NO completion:nil];
    }else{
        TTAlert(@"请先设置邮箱");
    }
    
}
- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error {
    [self dismissViewControllerAnimated:NO completion:nil];
}
- (void) openPBNavigationView{
    TTOpenURL([NSString stringWithFormat:@"peacebird://pbNavigation/%@/%i",[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],self.index]);
}
- (void) sendWXContent
{
    [self hideShareView];
    if(![WXApi isWXAppInstalled]){
        TTAlertNoTitle(@"本设备还未安装微信,无法使用微信分享功能");
        return;
    }
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
-(int) getFont:(int) defaultFont ip6Offset:(int)ip6Offset ip6pOffset:(int)ip6pOffset{
    if(IS_IPHONE4||IS_IPHONE5){
        return defaultFont;
    }else if(IS_IPHONE6){
        return ip6Offset+defaultFont;
    }else if(IS_IPHONE6_PLUS){
        return ip6pOffset+defaultFont;
    }else{
        return defaultFont;
    }
}
- (void)dealloc
{
    _contentView.previous = nil;
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_contentView);
    TT_RELEASE_SAFELY(_shareView);
    TT_RELEASE_SAFELY(_backgroundShareView);
    TT_RELEASE_SAFELY(_preScreenShot);
    TT_RELEASE_SAFELY(_selectedDate);
    TT_RELEASE_SAFELY(_initPeriod);
    
    TT_RELEASE_SAFELY(_calendarLabel);
    TT_RELEASE_SAFELY(_direction);
    [super dealloc];
}

#pragma mark - UIViewControllerTransitioningDelegate Methods

- (id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
    return self;
}

- (id <UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
    return self;
}

- (id <UIViewControllerInteractiveTransitioning>)interactionControllerForPresentation:(id <UIViewControllerAnimatedTransitioning>)animator {
    return nil;
}

- (id <UIViewControllerInteractiveTransitioning>)interactionControllerForDismissal:(id <UIViewControllerAnimatedTransitioning>)animator {
    return nil;
}

#pragma mark - UIViewControllerAnimatedTransitioning Methods

- (void)animationEnded:(BOOL)transitionCompleted {
    
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
    // Used only in non-interactive transitions, despite the documentation
    return 0.3f;
}

- (void)animateTransition:(id <UIViewControllerContextTransitioning>)transitionContext {
    UIViewController *fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    SwitchViewController *toViewController = (SwitchViewController*)[transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    
    CGRect endFrame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

  
        // The order of these matters – determines the view hierarchy order.
    
    [transitionContext.containerView addSubview:fromViewController.view];
    [transitionContext.containerView addSubview:toViewController.view];
    CGRect startFrame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    if([self.direction isEqualToString:@"left"]){
        startFrame.origin.x=SCREEN_WIDTH;
    }else{
        startFrame.origin.x=-SCREEN_WIDTH;
    }
        toViewController.view.frame = startFrame;

   
        [UIView animateWithDuration:[self transitionDuration:transitionContext] animations:^{
            toViewController.view.frame = endFrame;
        } completion:^(BOOL finished) {
            [transitionContext completeTransition:YES];
        }];

    
}


@end
