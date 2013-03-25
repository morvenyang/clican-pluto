//
//  QQVideoViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQVideoViewController.h"
#import "AtvUtil.h"
#import "VideoItem.h"
#import "AppDelegate.h"

@implementation QQVideoViewController

@synthesize video = _video;
@synthesize qqVideoRequestModel = _qqVideoRequestModel;
@synthesize summaryTextLabel = _summaryTextLabel;
@synthesize descriptionTextLabel = _descriptionTextLabel;
@synthesize playButton = _playButton;
@synthesize imageView = _imageView;
@synthesize reflectImageView = _reflectImageView;
@synthesize tableView = _tableView;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)init {
    if ((self = [self initWithNibName:nil bundle:nil])) {
        
    }
    return self;
}

- (id) initWithVid:(NSString*) vid{
    if ((self = [super init])) {
        self.video = nil;
        self.qqVideoRequestModel = [[QQVideoRequestModel alloc] initWithVid:vid delegate:self];
        self.reflectImageView = [[UIView alloc] initWithFrame:CGRectMake(10, 10 , 113, 164)];
        
        self.imageView = [[[TTImageView alloc] autorelease] initWithFrame:CGRectZero];
        self.imageView.frame = CGRectMake(0, 0, 93, 124);
        self.imageView.layer.cornerRadius = 8;
        self.imageView.layer.masksToBounds = YES;
        
        self.summaryTextLabel = [[[TTStyledTextLabel alloc] init] autorelease];
        
        self.summaryTextLabel.contentMode = UIViewContentModeCenter;
        self.summaryTextLabel.frame = CGRectMake(110,10,200,164);
        
        self.descriptionTextLabel = [[[TTStyledTextLabel alloc] init] autorelease];
        
        self.playButton=[UIButton buttonWithType:UIButtonTypeCustom];
        
        self.playButton.contentMode = UIViewContentModeCenter;
        [self.playButton addTarget:self action:@selector(playAction) forControlEvents: UIControlEventTouchUpInside];
        
    }             
    return self;
}

- (void) videoDidFinishLoad:(Video*) video{
    self.video = video;
    
    CGRect frame = [UIScreen mainScreen].applicationFrame;
    UIScrollView* scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height - 92)];
    
    
    NSLog(@"picurl=%@",[video picUrl]);
    self.imageView.delegate = self;
    self.imageView.urlPath = [video picUrl];
    self.summaryTextLabel.text = [TTStyledText textFromXHTML:[@"" stringByAppendingFormat:@"<strong>%@</strong>\n导演:%@\n主演:%@\n年份:%@ 地区:%@",video.title,video.directors,video.actors,video.year,video.area] lineBreaks:YES URLs:NO];
    [self.summaryTextLabel sizeToFit];
    double y = self.summaryTextLabel.frame.origin.y+self.summaryTextLabel.frame.size.height;
    
    [scrollView addSubview:self.reflectImageView];
    [scrollView addSubview:self.summaryTextLabel];
    
    
    if([self.video.videoItemList count]==1){
        y=y+10;
        self.playButton.frame = CGRectMake(110, y, 50, 50);
        [self.playButton setTitle:@"播放" forState:UIControlStateNormal];
        self.playButton.backgroundColor = RGBCOLOR(256,20,147);
        [self.playButton sizeToFit];
        [scrollView addSubview:self.playButton];
    }else{
        y = y+30;
        self.tableView = [[TTTableView alloc] initWithFrame:CGRectMake(0, y, frame.size.width, frame.size.height - y -92) style:UITableViewStylePlain];
        self.tableView.delegate=self;
        NSMutableArray* items = [NSMutableArray array];
        for(int i=0;i<[self.video.videoItemList count];i++){
            VideoItem* vi = [self.video.videoItemList objectAtIndex:i];
            NSString* actionUrl = [NSString stringWithFormat:@"atvserver://qq/play/%@/%@",vi.itemId,self.video.vid];
            TTTableTextItem* item = [TTTableTextItem itemWithText:vi.title URL:actionUrl];
            NSLog(@"title:%@,url:%@",vi.title,actionUrl);
            [items addObject:item];
        }
        self.tableView.dataSource = [[TTListDataSource alloc] initWithItems:items];
        [scrollView addSubview:self.tableView];
    }
    
    [self.view addSubview:scrollView];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableTextItem* item = [ds.items objectAtIndex:indexPath.item];
    TTOpenURL(item.URL);
}
- (void)imageView:(TTImageView*)imageView didLoadImage:(UIImage*)image{
    if(imageView == self.imageView){
        [AtvUtil markReflect:self.reflectImageView.layer image:self.imageView.image];
    }
}

- (void) playAction {
    if(true){
        [[AppDele jsEngine] runJS:@"appletv.showInputTextPage('a','b',function(content){},'','');"];
        return;
    }
    NSLog(@"Play video %@",self.video.title);
    if([self.video.videoItemList count]>0){
        VideoItem* vi = [self.video.videoItemList objectAtIndex:0];
        NSString* actionUrl = [NSString stringWithFormat:@"atvserver://qq/play/%@/%@",vi.itemId,self.video.vid];
        NSLog(@"playUrl:%@",actionUrl);
        TTOpenURL(actionUrl);
    }
}

- (void)videoDidStartLoad:(NSString*)vid{
    NSLog(@"load video vid:%@",vid);
}

- (void)video:(NSString*)vid didFailLoadWithError:(NSError*)error{
    NSLog(@"load video failutre vid:%@, error:%@",vid,[error description]);
}

- (void)loadView
{
    [super loadView];
    [self.qqVideoRequestModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (void)dealloc
{
    _qqVideoRequestModel.delegate = nil;
    TT_RELEASE_SAFELY(_video);
    TT_RELEASE_SAFELY(_qqVideoRequestModel);
    TT_RELEASE_SAFELY(_summaryTextLabel);
    TT_RELEASE_SAFELY(_descriptionTextLabel);
    TT_RELEASE_SAFELY(_playButton);
    TT_RELEASE_SAFELY(_imageView);
    TT_RELEASE_SAFELY(_reflectImageView);
    TT_RELEASE_SAFELY(_tableView);
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
