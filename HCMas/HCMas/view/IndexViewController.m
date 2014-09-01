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
@synthesize menuBgImageViews= _menuBgImageViews;
@synthesize menuButtonViews = _menuButtonViews;
@synthesize topImageView = _topImageView;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.imageIndex = 1;
        self.pointImageViews = [NSMutableArray array];
        self.menuBgImageViews =[NSMutableArray array];
        self.menuButtonViews =[NSMutableArray array];
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
    [self.view addSubview:[self createMenuView]];
    [self.view addSubview:[self createSettingView]];
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
-(void)selectMenu:(id*)sender{
    UIButton* buttonView = (UIButton*)sender;
    long index = [self.menuButtonViews indexOfObject:buttonView];
    for(int i=0;i<self.menuBgImageViews.count;i++){
        UIImageView* bgImageView = [self.menuBgImageViews objectAtIndex:i];
        if(i==index){
            bgImageView.image = [UIImage imageNamed:@"bg_press.png"];
        }else{
            bgImageView.image = [UIImage imageNamed:@"bg_nopress.png"];
        }
    }
}
-(UIView*)createMenuView{
    UIScrollView* menuView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, 200, 320, 130)] autorelease];
    NSArray* imageArray = [NSArray arrayWithObjects:@"logo_xtgl.png",@"logo_ksw.png",@"logo_zxgt.png",@"logo_dqwd.png",@"logo_dqsd.png",@"logo_dqyl.png",@"logo_fs.png",@"logo_fx.png",@"logo_yl.png",@"logo_bmwy.png",@"logo_jrx.png",@"logo_nbwy.png",@"logo_sl.png",@"logo_tyl.png",@"logo_thsl.png",@"logo_lf.png",@"logo_rxwy.png",@"logo_wd.png", nil];
    NSArray* nameArray = [NSArray arrayWithObjects:@"系统设置",@"库水位",@"干滩",@"大气温度",@"大气湿度",@"大气气压",@"风速",@"风向",@"雨量",@"表面位移",@"浸润线",@"内部位移",@"滲流",@"土压力",@"土壤含水率",@"裂缝",@"柔性位移",@"土壤温度", nil];
    
    
    for(int i=0;i<imageArray.count;i++){
        int yoffset = 0;
        long xoffset = 0;
        if(i>=imageArray.count/2){
            yoffset = 62;
            xoffset =-imageArray.count/2;
        }
        
        NSString* image =[imageArray objectAtIndex:i];
        NSString* name =[nameArray objectAtIndex:i];
        UIButton* buttonView = [[UIButton alloc] initWithFrame:CGRectMake(16+(i+xoffset)*72, 5+yoffset, 40, 40)];
        [buttonView setImage:[UIImage imageNamed:image] forState:UIControlStateNormal];
        [buttonView addTarget:self action:@selector(selectMenu:) forControlEvents:UIControlEventTouchUpInside];
        UIImageView* bgImageView = [self createImageViewFromNamedImage:@"bg_nopress.png" frame:CGRectMake(1+(i+xoffset)*72, 0+yoffset, 70, 60)];
        [self.menuBgImageViews addObject:bgImageView];
        [self.menuButtonViews addObject:buttonView];
        [menuView addSubview:bgImageView];
        [menuView addSubview:buttonView];
        [menuView addSubview:[self createLabel:name frame:CGRectMake(2+(i+xoffset)*72, 45+yoffset, 70, 12) textColor:@"#000000" font:12 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    }
    
    
    menuView.contentSize =
    CGSizeMake(imageArray.count/2*72+2, 130);
    return menuView;
}

-(UIView*)createSettingView{
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    UIScrollView* settingView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, 330, 320, height-330-30)] autorelease];
    settingView.contentSize = CGSizeMake(320, 400);
    TTTableView* tableView = [[[TTTableView alloc] initWithFrame:CGRectMake(0, 0, 320, 400)] autorelease];
    tableView.scrollEnabled=NO;

    TTSectionedDataSource* ds = [TTSectionedDataSource dataSourceWithObjects:
        @"设置",
        [self createTableItemByTitle:@"服务地址" subTitle:@"请正确填写服务端地址" action:nil],
        [self createTableItemByTitle:@"工程项目" subTitle:@"请勾选需要查看的工程" action:nil],
        [self createTableItemByTitle:@"更新频率" subTitle:@"设置刷新周期(秒)" action:nil],
        @"报警模式",
        [self createTableItemByTitle:@"报警开/关" subTitle:@"请勾选是否开启报警" action:nil],
        [self createTableItemByTitle:@"情景模式" subTitle:@"报警情景模式" action:nil],
        @"关于",
        [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow3\">软件名称:华测自动化监测与预警系统（手机版）</span><br/><span class=\"settingRow3\">版本:1.0</span><br/><span class=\"settingRow3\">版权:上海华测导航技术有限公司</span><br/><span class=\"settingRow3\">网址:www.huace.cn</span>"] lineBreaks:YES URLs:YES]],
                                 nil];
    tableView.dataSource = ds;
    tableView.delegate = self;
    tableView.backgroundColor = [UIColor blackColor];
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        tableView.separatorInset = UIEdgeInsetsZero;
    }
    #endif
    tableView.separatorColor = [UIColor blackColor];
    tableView.sectionIndexBackgroundColor=[UIColor blackColor];
    [settingView addSubview:tableView];
    return settingView;
}
-(TTTableStyledTextItem*)createTableItemByTitle:(NSString*) title subTitle:(NSString*) subTitle action:(SEL)action{
    TTTableStyledTextItem* item=[TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow1\">%@</span><br/><span class=\"settingRow2\">%@</span>",title,subTitle] lineBreaks:YES URLs:YES]];
    item.delegate = self;
    item.selector = action;
    return item;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==0&&indexPath.section==2){
        return 100;
    }else{
        return 50;
    }
}
- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([cell isKindOfClass:[TTStyledTextTableItemCell class]]){
        TTStyledTextTableItemCell* itemCell = (TTStyledTextTableItemCell*)cell;
        itemCell.label.backgroundColor = [UIColor clearColor];
        itemCell.backgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"main_background.png"]];
    }
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

- (void)dealloc
{
    TT_RELEASE_SAFELY(_pointImageViews);
    TT_RELEASE_SAFELY(_topImageView);
    TT_RELEASE_SAFELY(_menuBgImageViews);
    TT_RELEASE_SAFELY(_menuButtonViews);
    [super dealloc];
}

@end
