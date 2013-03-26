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
@implementation XmlViewController

@synthesize xml = _xml;
@synthesize type = _type;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}
-(id) initWithXml:(NSString*) xml{
    self = [super init];
    if(self){
        self.xml = xml;
    }
    return self;
}
- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_xml);
    [super dealloc];
}
-(void) loadView{
    @try{
        [super loadView];
        NSError *theError = NULL;
        CXMLDocument *document = [[[CXMLDocument alloc] initWithXMLString:self.xml options:0 error:&theError] autorelease];
        CXMLElement* rootElement=[document rootElement];
        CXMLElement* bodyElement = [[rootElement elementsForName:@"body"] objectAtIndex:0];
        for(int i=0;i<[bodyElement childCount];i++){
            CXMLNode* node = [bodyElement childAtIndex:i];
            if([[node name] isEqualToString:@"listScrollerSplit"]){
                CXMLElement* listScrollerSplitElement = (CXMLElement*)node;
                self.type = @"listScrollerSplit";
                CXMLElement* titleElement=(CXMLElement*)[listScrollerSplitElement nodeForXPath:@"header/simpleHeader/title" error:nil];
                self.title = [titleElement stringValue];
                
                CGRect frame = [UIScreen mainScreen].applicationFrame;
                UIScrollView* scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height - 92)];
                
                TTTableView* tableView = [[TTTableView alloc] initWithFrame:scrollView.frame style:UITableViewStylePlain];
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
                
                [scrollView addSubview:tableView];
                [self.view addSubview:scrollView];
                break;
            }else if([[node name] isEqualToString:@"scroller"]){
                CXMLElement* scrollerElement = (CXMLElement*)node;
                NSString* idStr = [[scrollerElement attributeForName:@"id"] stringValue];
                if([idStr isEqualToString:@"index"]){
                    self.type = @"index";
                    self.title = @"视频";
                    
                    CGRect frame = [UIScreen mainScreen].applicationFrame;
                    UIScrollView* scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height - 92)];
                    
                    TTTableView* tableView = [[TTTableView alloc] initWithFrame:scrollView.frame style:UITableViewStylePlain];
                    
                    
                    NSMutableArray* items = [NSMutableArray array];
                    NSArray* grids = [scrollerElement nodesForXPath:@"items/grid" error:nil];
                    CXMLElement* movieGridElement = nil;
                    for(int i=0;i<[grids count];i++){
                        CXMLElement* item = (CXMLElement*)[grids objectAtIndex:i];
                        if([[[item attributeForName:@"id"] stringValue] isEqualToString:@"movieGrid"]){
                            movieGridElement = item;
                            break;
                        }
                    }
                    if(movieGridElement!=nil){
                        NSArray* movies = [movieGridElement nodesForXPath:@"items/moviePoster" error:nil];
                        
                        for(int i=0;i<[movies count];i=i+3){
                            NSMutableArray* cellItems = [NSMutableArray array];
                            for(int j=i;j<i+3&&j<[movies count];j++){
                                CXMLElement* moviePoster = (CXMLElement*)[movies objectAtIndex:j];
                                CXMLElement* title = (CXMLElement*)[moviePoster nodeForXPath:@"title" error:nil];
                                CXMLElement* image = (CXMLElement*)[moviePoster nodeForXPath:@"image" error:nil];
                                NSString* onSelect = [[moviePoster attributeForName:@"onSelect"] stringValue];
                                NSLog(@"onSelect=%@",onSelect);
                                
                                Video* video = [[[Video alloc] init] autorelease];
                                video.title = [title stringValue];
                                video.picUrl =[image stringValue];
                                video.onSelect = onSelect;
                                [cellItems addObject:video];
                            }
                            [items addObject:[VideoTableItem itemWithVideoList:cellItems]];
                        }
                    }
                    tableView.dataSource = [[XmlDataSource alloc] initWithItems:items];
                    tableView.delegate=self;
                    
                    [scrollView addSubview:tableView];
                    [self.view addSubview:scrollView];
                    break;
                }
            }
        }
    }@catch(NSException* e){
        ALog(@"error occured:%@",[e description]);
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableTextItem* item = [ds.items objectAtIndex:indexPath.item];
    NSString* script = item.URL;
    [[AppDele jsEngine] runJS:script];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableItem* item = [ds.items objectAtIndex:indexPath.item];
    if([item isKindOfClass:[VideoTableItem class]]){
        return 180;
    }else{
        return 50;
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

@end
