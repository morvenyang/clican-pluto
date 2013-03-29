//
//  XmlViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "XmlViewController.h"
#import "TouchXML.h"
#import "AtvHeader.h"
#import "AtvOneLineMenuItem.h"
#import "AtvListScrollerSplit.h"
#import "AppDelegate.h"
#import "Video.h"
#import "VideoItem.h"
#import "VideoTableItem.h"
#import "VideoTableItemCell.h"
#import "XmlDataSource.h"
#import "Constants.h"
#import "AtvUtil.h"
@implementation XmlViewController

@synthesize xml = _xml;
@synthesize script = _script;
@synthesize type = _type;
@synthesize append = _append;
@synthesize videos = _videos;
@synthesize summaryTextLabel = _summaryTextLabel;
@synthesize descriptionTextLabel = _descriptionTextLabel;
@synthesize playButton = _playButton;
@synthesize imageView = _imageView;
@synthesize reflectImageView = _reflectImageView;
@synthesize scrollView = _scrollView;
@synthesize playerViewController = _playerViewController;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        CGRect frame = [UIScreen mainScreen].applicationFrame;
        self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height - 92)];
    }
    return self;
}
-(id) initWithXml:(NSString*) xml{
    self = [super init];
    if(self){
        self.xml = xml;
        self.videos = [NSMutableArray array];
    }
    return self;
}
-(id) initWithScript:(NSString*) script{
    self = [super init];
    if(self){
        self.videos = [NSMutableArray array];
        self.script = script;
    }
    return self;
}
- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_xml);
    TT_RELEASE_SAFELY(_type);
    TT_RELEASE_SAFELY(_videos);
    
    TT_RELEASE_SAFELY(_summaryTextLabel);
    TT_RELEASE_SAFELY(_descriptionTextLabel);
    TT_RELEASE_SAFELY(_playButton);
    TT_RELEASE_SAFELY(_imageView);
    TT_RELEASE_SAFELY(_reflectImageView);
    TT_RELEASE_SAFELY(_scrollView);
    TT_RELEASE_SAFELY(_playerViewController);
    TT_RELEASE_SAFELY(_progressHUD);
    [super dealloc];
}

-(void) appendXml:(NSString*) xml{
    if(xml==nil){
        return;
    }
    @try{
        NSError *theError = NULL;
        CXMLDocument *document = [[[CXMLDocument alloc] initWithXMLString:xml options:0 error:&theError] autorelease];
        CXMLElement* rootElement=[document rootElement];
        CXMLElement* bodyElement = [[rootElement elementsForName:@"body"] objectAtIndex:0];
        for(int i=0;i<[bodyElement childCount];i++){
            CXMLNode* node = [bodyElement childAtIndex:i];
            if([[node name] isEqualToString:@"listScrollerSplit"]){
                [self displayListScrollerSplit:node];
                break;
            }if([[node name] isEqualToString:@"listWithPreview"]){
                [self displayListScrollerSplit:node];
                break;
            }else if([[node name] isEqualToString:@"scroller"]){
                [self appendVideos:node];
                break;
            }else if([[node name] isEqualToString:@"itemDetail"]){
                [self displayDetail:node];
                break;
            }else if([[node name] isEqualToString:@"dialog"]){
                [self performSelectorOnMainThread:@selector(displayDialog:) withObject:node waitUntilDone:YES];
                break;
            }else if([[node name] isEqualToString:@"optionDialog"]){
                [self displayListScrollerSplit:node];
                break;
            }else if([[node name] isEqualToString:@"videoPlayer"]){
                [self performSelectorOnMainThread:@selector(playVideo:) withObject:node waitUntilDone:YES];
                break;
                break;
            }else if([[node name] isEqualToString:@"listByNavigation"]){
                [self displayListByNavigation:node];
                break;
            }
            
        }
    }@catch(NSException* e){
        ALog(@"error occured:%@",[e description]);
    }@finally {
        [self.progressHUD hide:NO];
    }
}
-(void)playVideo:(CXMLNode*) node{
    NSString* mediaUrl = [[node nodeForXPath:@"httpLiveStreamingVideoAsset/mediaURL" error:nil] stringValue];
    NSLog(@"mediaUrl:%@",mediaUrl);
    self.playerViewController = [[[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL URLWithString:mediaUrl]] autorelease];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification object:self.playerViewController.moviePlayer];
    
    self.playerViewController.moviePlayer.controlStyle = MPMovieControlStyleFullscreen;
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    [self presentMoviePlayerViewControllerAnimated:self.playerViewController];
    [self.playerViewController.moviePlayer prepareToPlay];
    [self.playerViewController.moviePlayer setFullscreen:YES animated:YES];
    [self.playerViewController.moviePlayer play];
}
- (void)moviePlayBackDidFinish:(NSNotification*)notification {

        //user hit the done button
        MPMoviePlayerController *moviePlayer = [notification object];
        
        [[NSNotificationCenter defaultCenter] removeObserver:self
                                                        name:MPMoviePlayerPlaybackDidFinishNotification
                                                      object:moviePlayer];
        
        if ([moviePlayer respondsToSelector:@selector(setFullscreen:animated:)]) {
            [moviePlayer.view removeFromSuperview];
        }
        [self.navigationController setNavigationBarHidden:NO animated:YES];
        [self.navigationController popViewControllerAnimated:YES];
 
}

-(void) displayListScrollerSplit:(CXMLNode*) node{
    CXMLElement* listScrollerSplitElement = (CXMLElement*)node;
    self.type = @"listScrollerSplit";
    CXMLElement* titleElement=(CXMLElement*)[listScrollerSplitElement nodeForXPath:@"header/simpleHeader/title" error:nil];
    self.title = [titleElement stringValue];
    
    TTTableView* tableView = [[TTTableView alloc] initWithFrame:self.scrollView.frame style:UITableViewStylePlain];
    tableView.delegate=self;
    
    NSMutableArray* items = [NSMutableArray array];
    CXMLElement* itemsElement=(CXMLElement*)[listScrollerSplitElement nodeForXPath:@"menu/sections/menuSection/items" error:nil];
    
    for(int i=0;i<[itemsElement childCount];i++){
        CXMLNode* node = [itemsElement childAtIndex:i];
        if([[node name] isEqualToString:@"oneLineMenuItem"]){
            CXMLElement* oneLineMenuItemElement = (CXMLElement*)node;
            NSString* onSelect = [[oneLineMenuItemElement attributeForName:@"onSelect"] stringValue];
            NSLog(@"onSelect=%@",onSelect);
            CXMLElement* labelElement = (CXMLElement*)[oneLineMenuItemElement nodeForXPath:@"label" error:nil];
            TTTableTextItem* item = [TTTableTextItem itemWithText:[labelElement stringValue] URL:onSelect];
            [items addObject:item];
        }
        
    }
    tableView.dataSource = [[XmlDataSource alloc] initWithItems:items];
    
    [self.scrollView addSubview:tableView];
    [self.view addSubview:self.scrollView];
}

-(void) displayListByNavigation:(CXMLNode*) node{
    CXMLElement* listScrollerSplitElement = (CXMLElement*)node;
    self.type = @"listScrollerSplit";
    CXMLElement* titleElement=(CXMLElement*)[listScrollerSplitElement nodeForXPath:@"header/thumblerWithSubtitle/subtitle" error:nil];
    self.title = [titleElement stringValue];
    
    TTTableView* tableView = [[TTTableView alloc] initWithFrame:self.scrollView.frame style:UITableViewStylePlain];
    tableView.delegate=self;
    
    NSMutableArray* items = [NSMutableArray array];
    NSArray* menuItems =[listScrollerSplitElement nodesForXPath:@"menu/sections/menuSection/items/oneLineMenuItem" error:nil];
    
    for(int i=0;i<[menuItems count];i++){
        CXMLNode* node = [menuItems objectAtIndex:i];
        if([[node name] isEqualToString:@"oneLineMenuItem"]){
            CXMLElement* oneLineMenuItemElement = (CXMLElement*)node;
            NSString* onSelect = [[oneLineMenuItemElement attributeForName:@"onSelect"] stringValue];
            NSLog(@"onSelect=%@",onSelect);
            CXMLElement* labelElement = (CXMLElement*)[oneLineMenuItemElement nodeForXPath:@"label" error:nil];
   
            TTTableStyledTextItem* item = [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[labelElement stringValue] lineBreaks:YES URLs:NO] URL:onSelect];
            [items addObject:item];
        }
        
    }
    tableView.dataSource = [[XmlDataSource alloc] initWithItems:items];
    
    [self.scrollView addSubview:tableView];
    [self.view addSubview:self.scrollView];
}
-(void) loadView{
    [super loadView];
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    [self appendXml:self.xml];
}

-(void) displayDetail:(CXMLNode*) node{
    Video* video = [[[Video alloc] init] autorelease];
    video.title = [[node nodeForXPath:@"title" error:nil] stringValue];
    video.description = [[node nodeForXPath:@"summary" error:nil] stringValue];
    video.picUrl = [[node nodeForXPath:@"image" error:nil] stringValue];
    
    NSArray* rows = [node nodesForXPath:@"table/rows/row" error:nil];
    for(int i=0;i<[rows count];i++){
        CXMLNode* row = [rows objectAtIndex:i];
        NSArray* labels = [row nodesForXPath:@"label" error:nil];
        for(int j=0;j<[labels count];j++){
            CXMLNode* label = [labels objectAtIndex:j];
            NSString* labelValue = [label stringValue];
            if(labelValue==NULL||[labelValue stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]].length==0){
                continue;
            }
            NSRange range = [labelValue rangeOfString:@":"];
            NSString* name = nil;
            NSString* value = nil;
            if(range.location!=NSNotFound){
                name = [labelValue substringWithRange:NSMakeRange(0, range.location)];
                if(range.location+1<[labelValue length]){
                    value = [labelValue substringFromIndex:range.location+1];
                    if([name isEqualToString:@"导演"]){
                        video.directors = value;
                    }else if([name isEqualToString:@"年代"]){
                        video.year = value;
                    }else if([name isEqualToString:@"地区"]){
                        video.area = value;
                    }else if([name isEqualToString:@"评分"]){
                        video.score = value;
                    }else if([name isEqualToString:@"主演"]){
                        video.actors = value;
                    }
                }
            }
        }
    }
    
    NSArray* actionItems = [node nodesForXPath:@"centerShelf/shelf/sections/shelfSection/items/actionButton" error:nil];
    NSMutableArray* videoItemList = [NSMutableArray array];
    video.videoItemList = videoItemList;
    for(int i=0;i<[actionItems count];i++){
         CXMLElement* actionButton = [actionItems objectAtIndex:i];
        NSString* onSelect = [[actionButton attributeForName:@"onSelect"] stringValue];
        NSString* title = [[actionButton nodeForXPath:@"title" error:nil] stringValue];
        VideoItem* vi = [[[VideoItem alloc] init] autorelease];
        vi.title = title;
        vi.onSelect = onSelect;
        [videoItemList addObject:vi];
    }
    CGRect frame = [UIScreen mainScreen].applicationFrame;

    
    
    NSLog(@"picurl=%@",[video picUrl]);
    self.reflectImageView = [[UIView alloc] initWithFrame:CGRectMake(10, 10 , 113, 164)];
    
    self.imageView = [[[TTImageView alloc] autorelease] initWithFrame:CGRectZero];
    self.imageView.frame = CGRectMake(0, 0, 93, 124);
    self.imageView.layer.cornerRadius = 8;
    self.imageView.layer.masksToBounds = YES;
    self.imageView.delegate = self;
    self.imageView.urlPath = [video picUrl];
    self.summaryTextLabel = [[[TTStyledTextLabel alloc] init] autorelease];
    
    self.summaryTextLabel.contentMode = UIViewContentModeCenter;
    self.summaryTextLabel.frame = CGRectMake(110,10,200,164);
    
    
    self.summaryTextLabel.text = [TTStyledText textFromXHTML:[@"" stringByAppendingFormat:@"<strong>%@</strong>\n导演:%@\n主演:%@\n年份:%@\n地区:%@",video.title,video.directors,video.actors,video.year,video.area] lineBreaks:YES URLs:NO];
    [self.summaryTextLabel sizeToFit];
    double y = self.summaryTextLabel.frame.origin.y+self.summaryTextLabel.frame.size.height;
    y = y+30;
    UIScrollView* descScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(10, y, frame.size.width-20, 70)];
    self.descriptionTextLabel = [[[TTStyledTextLabel alloc] init] autorelease];
    self.descriptionTextLabel.contentMode = UIViewContentModeCenter;
    self.descriptionTextLabel.frame = CGRectMake(0,0,frame.size.width-20,70);
    self.descriptionTextLabel.text=[TTStyledText textFromXHTML:[@"" stringByAppendingFormat:@"%@",video.description] lineBreaks:YES URLs:NO];
    [self.descriptionTextLabel sizeToFit];
    [descScrollView addSubview:self.descriptionTextLabel];
    [self.scrollView addSubview:self.reflectImageView];
    [self.scrollView addSubview:self.summaryTextLabel];
    [self.scrollView addSubview:descScrollView];
    y = y+ 70;
    TTTableView* tableView = [[TTTableView alloc] initWithFrame:CGRectMake(0, y, frame.size.width, frame.size.height - y -92) style:UITableViewStylePlain];
    tableView.delegate=self;
    NSMutableArray* items = [NSMutableArray array];
    for(int i=0;i<[video.videoItemList count];i++){
        VideoItem* vi = [video.videoItemList objectAtIndex:i];
        TTTableTextItem* item = [TTTableTextItem itemWithText:vi.title URL:vi.onSelect];
        [items addObject:item];
    }
    tableView.dataSource = [[TTListDataSource alloc] initWithItems:items];
    [self.scrollView addSubview:tableView];
    
    
    [self.view addSubview:self.scrollView];
}
-(void) displayDialog:(CXMLNode*)node{
    NSString* title = [[node nodeForXPath:@"title" error:nil] stringValue];
    self.title = title;
    NSString* desc= [[node nodeForXPath:@"description" error:nil] stringValue];
    
    CGRect frame = [UIScreen mainScreen].applicationFrame;
    UIWebView* descriptionTextLabel = [[[UIWebView alloc] init] autorelease];
    descriptionTextLabel.contentMode = UIViewContentModeCenter;
    descriptionTextLabel.frame = CGRectMake(0,0,frame.size.width-20,frame.size.height - 92);
    [descriptionTextLabel loadHTMLString:[NSString stringWithFormat:@"<h2>%@<h2/><br/><p>%@</p>",title,desc] baseURL:nil];
    [descriptionTextLabel setScalesPageToFit:YES];
    [self.scrollView addSubview:descriptionTextLabel];
    [self.view addSubview:self.scrollView];
}

-(void) appendVideos:(CXMLNode*) node{
    if(!self.append){
        [self.videos removeAllObjects];
    }
    
    CXMLElement* scrollerElement = (CXMLElement*)node;
    NSString* idStr = [[scrollerElement attributeForName:@"id"] stringValue];
    if([idStr isEqualToString:@"index"]){
        self.type = @"index";
        self.title = @"视频";

        
        TTTableView* tableView = [[TTTableView alloc] initWithFrame:self.scrollView.frame style:UITableViewStylePlain];
        
        
        NSMutableArray* items = [NSMutableArray array];
        NSArray* grids = [scrollerElement nodesForXPath:@"items/grid" error:nil];
        CXMLElement* movieGridElement = nil;
        CXMLElement* pagingGridElement = nil;
        for(int i=0;i<[grids count];i++){
            CXMLElement* item = (CXMLElement*)[grids objectAtIndex:i];
            if([[[item attributeForName:@"id"] stringValue] isEqualToString:@"movieGrid"]){
                movieGridElement = item;
            }else if([[[item attributeForName:@"id"] stringValue] isEqualToString:@"pagingGrid"]){
                pagingGridElement = item;
            }
        }
        if(movieGridElement!=nil){
            NSArray* movies = [movieGridElement nodesForXPath:@"items/moviePoster" error:nil];
            for(int i=0;i<[movies count];i++){
                CXMLElement* moviePoster = (CXMLElement*)[movies objectAtIndex:i];
                CXMLElement* title = (CXMLElement*)[moviePoster nodeForXPath:@"title" error:nil];
                CXMLElement* image = (CXMLElement*)[moviePoster nodeForXPath:@"image" error:nil];
                NSString* onSelect = [[moviePoster attributeForName:@"onSelect"] stringValue];
                
                
                Video* video = [[[Video alloc] init] autorelease];
                video.title = [title stringValue];
                video.picUrl =[image stringValue];
                video.onSelect = onSelect;
                [self.videos addObject:video];
            }
            for(int i=0;i<[self.videos count];i=i+3){
                NSMutableArray* cellItems = [NSMutableArray array];
                for(int j=i;j<i+3&&j<[self.videos count];j++){
                    Video* video = [self.videos objectAtIndex:j];
                    [cellItems addObject:video];
                }
                VideoTableItem* vti = [VideoTableItem itemWithVideoList:cellItems];
                [items addObject:vti];
            }
            if(pagingGridElement!=nil&&[movies count]>0){
                CXMLElement* nextPageElement=nil;
                NSArray* actionButtons = [pagingGridElement nodesForXPath:@"items/actionButton" error:nil];
                for(int i=0;i<[actionButtons count];i++){
                    CXMLElement* item = (CXMLElement*)[actionButtons objectAtIndex:i];
                    if([[[item attributeForName:@"id"] stringValue] isEqualToString:@"nextPage"]){
                        nextPageElement = item;
                    }
                }
                if(nextPageElement!=nil){
                    NSString* onSelect = [[nextPageElement attributeForName:@"onSelect"] stringValue];
                    ALog(@"more on select:%@",onSelect);
                    TTTableTextItem* moreItem = [TTTableTextItem itemWithText:@"更多" URL:onSelect];
                    [items addObject:moreItem];
                }
            }
        }
        
        tableView.dataSource = [[XmlDataSource alloc] initWithItems:items];
       
        tableView.delegate=self;
        NSArray* subviws =[self.scrollView subviews];
        for(UIView* view in subviws){
            [view removeFromSuperview];
        }
        [self.scrollView addSubview:tableView];
        [self.view addSubview:self.scrollView];
        NSLog(@"add videos successfully");
    }

}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    self.append = NO;
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableItem* item = [ds.items objectAtIndex:indexPath.row];
    
    if([item isKindOfClass:[TTTableTextItem class]]){
        TTTableTextItem* tti = (TTTableTextItem*)item;
        NSString* script = tti.URL;
        if([tti.text isEqualToString:@"更多"]){
            self.append = YES;
        }
        if(self.append){
            self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
            self.progressHUD.delegate = self;
            self.progressHUD.labelText = @"加载中...";
            [self.view addSubview:self.progressHUD];
            [self.view bringSubviewToFront:self.progressHUD];
            [self.progressHUD show:YES];
            self.script = script;
            [NSThread detachNewThreadSelector:@selector(runJS:) toTarget:self withObject:self];
        }else{
            XmlViewController* controler = [[XmlViewController alloc] autorelease];
            [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:YES];
            [controler initWithScript:script];
            [NSThread detachNewThreadSelector:@selector(runJS:) toTarget:self withObject:controler];
        }
        
    }else if([item isKindOfClass:[TTTableStyledTextItem class]]){
        TTTableStyledTextItem* tti = (TTTableStyledTextItem*)item;
        NSString* script = tti.URL;
        //[[AppDele jsEngine] runJS:script];
        XmlViewController* controler = [[XmlViewController alloc] autorelease];
        [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:YES];
        [controler initWithScript:script];
        [NSThread detachNewThreadSelector:@selector(runJS:) toTarget:self withObject:controler];
    }else if([item isKindOfClass:[VideoTableItem class]]){
        ALog(@"select VideoTableItem");
    }
    
}
- (void) runJS:(id)object{
    [[AppDele jsEngine] runJS:((XmlViewController*)object).script];
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableItem* item = [ds.items objectAtIndex:indexPath.row];
    if([item isKindOfClass:[VideoTableItem class]]){
        return 180;
    }else{
        return 50;
    }
}

- (void)imageView:(TTImageView*)imageView didLoadImage:(UIImage*)image{
    if(imageView == self.imageView){
        [AtvUtil markReflect:self.reflectImageView.layer image:self.imageView.image];
    }
}

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
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

@end
